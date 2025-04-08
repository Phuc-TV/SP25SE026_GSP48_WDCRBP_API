package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.QuotationDetail;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.dto.QuotationDTO;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.QuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.QuotationDetailRes;
import SP25SE026_GSP48_WDCRBP_api.repository.QuotationDetailRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.RequestedProductRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.service.QuotationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationDetailServiceImpl implements QuotationDetailService {

    private final QuotationDetailRepository quotationDetailRepository;
    private final RequestedProductRepository requestedProductRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    @Override
    @Transactional
    public List<QuotationDetailRes> saveQuotationDetail(QuotationDetailRequest requestDTO) {
        RequestedProduct requestedProduct = requestedProductRepository.findById(requestDTO.getRequestedProductId())
                .orElseThrow(() -> new RuntimeException("RequestedProduct not found"));

        // Delete old quotations
        quotationDetailRepository.deleteByRequestedProduct(requestedProduct);

        float totalAmount = 0f;

        for (QuotationDTO dto : requestDTO.getQutotaions()) {
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

        // Prepare response (same as before)
        Map<Long, List<QuotationDetail>> groupedByProductId =
                quotationDetailRepository.findAll().stream()
                        .collect(Collectors.groupingBy(q -> q.getRequestedProduct().getRequestedProductId()));

        List<QuotationDetailRes> responseList = new ArrayList<>();

        for (Map.Entry<Long, List<QuotationDetail>> entry : groupedByProductId.entrySet()) {
            RequestedProduct rp = entry.getValue().get(0).getRequestedProduct();

            QuotationDetailRes.RequestedProductInfo productInfo =
                    QuotationDetailRes.RequestedProductInfo.builder()
                            .requestedProductId(rp.getRequestedProductId())
                            .quantity(rp.getQuantity())
                            .createdAt(rp.getCreatedAt())
                            .totalAmount(rp.getTotalAmount())
                            .hasDesign(rp.getHasDesign())
                            .category(rp.getCategory() != null ? rp.getCategory().toString() : null)
                            .build();

            List<QuotationDetailRes.QuotationInfo> detailList = entry.getValue().stream()
                    .map(q -> QuotationDetailRes.QuotationInfo.builder()
                            .quotId(q.getQuotationDetailId())
                            .costType(q.getCostType())
                            .costAmount(q.getCostAmount())
                            .quantityRequired(q.getQuantityRequired())
                            .build())
                    .collect(Collectors.toList());

            responseList.add(QuotationDetailRes.builder()
                    .requestedProduct(productInfo)
                    .quotationDetails(detailList)
                    .build());
        }

        return responseList;
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
                    .hasDesign(rp.getHasDesign())
                    .category(rp.getCategory() != null ? rp.getCategory().toString() : null)
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
}

