package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.GuaranteeOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.dto.QuotationDTO;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GuaranteeQuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.QuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.QuotationDetailRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.QuotationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationDetailServiceImpl implements QuotationDetailService {

    private final QuotationDetailRepository quotationDetailRepository;
    private final RequestedProductRepository requestedProductRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final GuaranteeOrderRepository guaranteeOrderRepository;
    private final OrderProgressRepository orderProgressRepository;
    private final ServiceDepositRepository serviceDepositRepository;
    private final OrderDepositRepository orderDepositRepository;

    @Override
    @Transactional
    public void saveQuotationDetail(QuotationDetailRequest requestDTO) {
        RequestedProduct requestedProduct = requestedProductRepository.findById(requestDTO.getRequestedProductId())
                .orElseThrow(() -> new RuntimeException("RequestedProduct not found"));

        // Delete old quotations
        quotationDetailRepository.deleteByRequestedProduct(requestedProduct);

        float totalAmount = 0f;

        for (QuotationDTO dto : requestDTO.getQuotations()) {
            QuotationDetail detail = QuotationDetail.builder()
                    .costType(dto.getCostType())
                    .costAmount(dto.getCostAmount())
                    .quantityRequired(dto.getQuantityRequired())
                    .requestedProduct(requestedProduct)
                    .build();
            quotationDetailRepository.save(detail);
            totalAmount += dto.getCostAmount();
        }

        // Update RequestedProduct
        requestedProduct.setTotalAmount(totalAmount);
        requestedProductRepository.save(requestedProduct);

        // Update ServiceOrder
        ServiceOrder serviceOrder = requestedProduct.getServiceOrder();
        if (serviceOrder != null) {
            List<RequestedProduct> allProducts = serviceOrder.getRequestedProducts();
            float orderTotal = (float) allProducts.stream()
                    .mapToDouble(RequestedProduct::getTotalAmount)
                    .sum();
            serviceOrder.setTotalAmount(orderTotal);
            serviceOrderRepository.save(serviceOrder);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationDetailRes> getAllByServiceOrderId(Long serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new RuntimeException("ServiceOrder not found"));

        List<RequestedProduct> requestedProducts = requestedProductRepository.findByServiceOrder(serviceOrder);

        List<QuotationDetailRes> result = new ArrayList<>();

        for (RequestedProduct rp : requestedProducts) {
            List<QuotationDetail> details = quotationDetailRepository.findByRequestedProduct(rp);

            QuotationDetailRes.RequestedProductInfo productInfo = QuotationDetailRes.RequestedProductInfo.builder()
                    .requestedProductId(rp.getRequestedProductId())
                    .quantity(rp.getQuantity())
                    .createdAt(rp.getCreatedAt())
                    .totalAmount(rp.getTotalAmount())
                    .category(rp.getCategory() != null ? rp.getCategory().getCategoryName() : null)
                    .build();

            List<QuotationDetailRes.QuotationInfo> quotationInfos = details.stream()
                    .map(d -> QuotationDetailRes.QuotationInfo.builder()
                            .quotId(d.getQuotationDetailId())
                            .costType(d.getCostType())
                            .costAmount(d.getCostAmount())
                            .quantityRequired(d.getQuantityRequired())
                            .build())
                    .collect(Collectors.toList());

            result.add(QuotationDetailRes.builder()
                    .requestedProduct(productInfo)
                    .quotationDetails(quotationInfos)
                    .build());
        }

        return result;
    }

    @Override
    @Transactional
    public void saveQuotationDetailForGuarantee(GuaranteeQuotationDetailRequest requestDTO) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findById(requestDTO.getGuaranteeOrderId())
                .orElseThrow(() -> new RuntimeException("RequestedProduct not found"));

        quotationDetailRepository.deleteByGuaranteeOrder(guaranteeOrder);

        float totalAmount = Optional.ofNullable(guaranteeOrder.getShipFee()).orElse(0f);

        for (QuotationDTO dto : requestDTO.getQuotations()) {
            QuotationDetail detail = QuotationDetail.builder()
                    .costType(dto.getCostType())
                    .costAmount(dto.getCostAmount())
                    .quantityRequired(dto.getQuantityRequired())
                    .guaranteeOrder(guaranteeOrder)
                    .build();
            quotationDetailRepository.save(detail);
            totalAmount += dto.getCostAmount();
        }

        if (guaranteeOrder.getStatus().equals(GuaranteeOrderStatusConstant.DA_DUYET_LICH_HEN)) {
            guaranteeOrder.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_BAO_GIA);

            OrderProgress newOrderProgress = new OrderProgress();
            newOrderProgress.setGuaranteeOrder(guaranteeOrder);
            newOrderProgress.setCreatedTime(LocalDateTime.now());
            newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_BAO_GIA);
            orderProgressRepository.save(newOrderProgress);
        }
        guaranteeOrder.setTotalAmount(totalAmount);
        guaranteeOrder.setAmountPaid(0f);
        guaranteeOrder.setAmountRemaining(totalAmount);
        guaranteeOrder.setRole("Customer");
        guaranteeOrder.setFeedback("");
        guaranteeOrderRepository.save(guaranteeOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotationDetailRes getAllByGuaranteeOrderId(Long guaranteeServiceId) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findById(guaranteeServiceId)
                .orElseThrow(() -> new RuntimeException("ServiceOrder not found"));

        QuotationDetailRes result = new QuotationDetailRes();

        List<QuotationDetail> details = quotationDetailRepository.findByGuaranteeOrder(guaranteeOrder);

        List<QuotationDetailRes.QuotationInfo> quotationInfos = details.stream()
                .map(d -> QuotationDetailRes.QuotationInfo.builder()
                        .quotId(d.getQuotationDetailId())
                        .costType(d.getCostType())
                        .costAmount(d.getCostAmount())
                        .quantityRequired(d.getQuantityRequired())
                        .build())
                .collect(Collectors.toList());

        result.setQuotationDetails(quotationInfos);

        return result;
    }

    @Override
    public void acceptQuotation(Long guaranteeOrderId) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(guaranteeOrderId);
        guaranteeOrder.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA);
        guaranteeOrder.setRole("Customer");
        guaranteeOrder.setFeedback("");

        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setGuaranteeOrder(guaranteeOrder);
        newOrderProgress.setCreatedTime(LocalDateTime.now());
        newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA);

        Short numberOfDeposits = guaranteeOrder.getAvailableService().getService().getNumberOfDeposits();

        for (int i = 0; i < numberOfDeposits; i++)
        {
            OrderDeposit orderDeposit = new OrderDeposit();

            int depositNumber = i + 1;
            ServiceDeposit serviceDeposit =
                    serviceDepositRepository.findServiceDepositsByService(guaranteeOrder.getAvailableService().getService()).stream().filter(item -> item.getDepositNumber() == depositNumber).findFirst().get();

            float percent = (float) serviceDeposit.getPercent() / 100;
            float totalAmount = guaranteeOrder.getTotalAmount();

            orderDeposit.setAmount(percent * totalAmount);
            orderDeposit.setDepositNumber(serviceDeposit.getDepositNumber());
            orderDeposit.setPercent(serviceDeposit.getPercent());
            orderDeposit.setGuaranteeOrder(guaranteeOrder);
            orderDeposit.setCreatedAt(LocalDateTime.now());

            orderDepositRepository.save(orderDeposit);
        }

        guaranteeOrderRepository.save(guaranteeOrder);
        orderProgressRepository.save(newOrderProgress);
    }
}

