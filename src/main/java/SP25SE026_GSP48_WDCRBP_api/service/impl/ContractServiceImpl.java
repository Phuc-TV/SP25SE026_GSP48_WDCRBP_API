package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WwCreateContractCustomizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ContractDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.RequestedProductListItemRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ContractService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ServiceOrderRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;

    @Autowired
    private OrderDepositRepository orderDepositRepository;

    @Autowired
    private ServiceDepositRepository serviceDepositRepository;

    @Autowired
    private RequestedProductRepository requestedProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ContractServiceImpl(ContractRepository contractRepository,
                               ServiceOrderRepository serviceRepository,
                               UserRepository userRepository,
                               OrderDepositRepository orDepositRepository,
                               OrderProgressRepository orderProgressRepository,
                               RequestedProductRepository requestedProductRepository)
    {
        this.contractRepository = contractRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.orderDepositRepository = orDepositRepository;
        this.requestedProductRepository = requestedProductRepository;
    }

    @Override
    public Contract createContractCustomize(WwCreateContractCustomizeRequest wwCreateContractCustomizeRequest)
    {
        ServiceOrder serviceOrder =
                serviceRepository.findServiceOrderByOrderId(wwCreateContractCustomizeRequest.getServiceOrderId());

        if (serviceOrder.getStatus().equals(ServiceOrderStatusConstant.DA_DUYET_LICH_HEN)) {
            OrderProgress newOrderProgress = new OrderProgress();
            newOrderProgress.setServiceOrder(serviceOrder);
            newOrderProgress.setCreatedTime(LocalDateTime.now());
            newOrderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_HOP_DONG);
            orderProgressRepository.save(newOrderProgress);
        }

        Contract contract1 = contractRepository.findContractByServiceOrder(serviceOrder);

        if (contract1 == null)
        {
            Contract contract = new Contract();
            for (int i = 0; i < wwCreateContractCustomizeRequest.getWarrantyDurations().size(); i++)
            {
                Short warrantyDuration = wwCreateContractCustomizeRequest.getWarrantyDurations().get(i);
                RequestedProduct requestedProduct =
                        requestedProductRepository.findRequestedProductByRequestedProductId(wwCreateContractCustomizeRequest.getRequestedProductIds().get(i));
                requestedProduct.setWarrantyDuration(warrantyDuration);
            }
            contract.setCompleteDate(wwCreateContractCustomizeRequest.getCompleteDate());
            contract.setWoodworkerSignature(wwCreateContractCustomizeRequest.getWoodworkerSignature());
            contract.setSignDate(LocalDateTime.now());
            contract.setWoodworkerTerms(wwCreateContractCustomizeRequest.getWoodworkerTerms());
            contract.setAgreement(wwCreateContractCustomizeRequest.getAgreement());

            List<RequestedProduct> requestedProduct =
                    requestedProductRepository.findRequestedProductByServiceOrder(serviceOrder);

            float i =0;
            for (RequestedProduct rp : requestedProduct)
            {
                i = i+ rp.getTotalAmount();
            }
            i = i + Optional.ofNullable(serviceOrder.getShipFee()).orElse(0f);

            serviceOrder.setFeedback("");
            serviceOrder.setAmountPaid((float) 0);
            serviceOrder.setTotalAmount(i);
            serviceOrder.setAmountRemaining(i);
            serviceOrder.setRole("Customer");
            serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_HOP_DONG);
            serviceRepository.save(serviceOrder);

            contract.setContractTotalAmount(i);
            contract.setServiceOrder(serviceOrder);
            contractRepository.save(contract);

            serviceRepository.save(serviceOrder);
            return contract;
        }
        else
        {
            if (wwCreateContractCustomizeRequest.getCompleteDate() != null)
                contract1.setCompleteDate(wwCreateContractCustomizeRequest.getCompleteDate());
            if (wwCreateContractCustomizeRequest.getAgreement() != null)
                contract1.setAgreement(wwCreateContractCustomizeRequest.getAgreement());
            for (int i = 0; i < wwCreateContractCustomizeRequest.getWarrantyDurations().size(); i++)
            {
                Short warrantyDuration = wwCreateContractCustomizeRequest.getWarrantyDurations().get(i);
                RequestedProduct requestedProduct =
                        requestedProductRepository.findRequestedProductByRequestedProductId(wwCreateContractCustomizeRequest.getRequestedProductIds().get(i));
                requestedProduct.setWarrantyDuration(warrantyDuration);
            }
            if (wwCreateContractCustomizeRequest.getWoodworkerTerms() != null)
                contract1.setWoodworkerTerms(wwCreateContractCustomizeRequest.getWoodworkerTerms());
            List<RequestedProduct> requestedProduct =
                    requestedProductRepository.findRequestedProductByServiceOrder(serviceOrder);
            float i =0;
            for (RequestedProduct rp : requestedProduct)
            {
                i = i+ rp.getTotalAmount();
            }
            i = i + Optional.ofNullable(serviceOrder.getShipFee()).orElse(0f);
            contract1.setContractTotalAmount(i);
            contractRepository.save(contract1);

            serviceOrder.setFeedback("");
            serviceOrder.setRole("Customer");
            serviceOrder.setAmountPaid((float) 0);
            serviceOrder.setTotalAmount(i);
            serviceOrder.setAmountRemaining(i);
            serviceRepository.save(serviceOrder);

            return contract1;
        }
    }

    public void createOrderDeposit(ServiceOrder serviceOrder) {
        Short numberOfDeposits = serviceOrder.getAvailableService().getService().getNumberOfDeposits();

        for (int i = 0; i < numberOfDeposits; i++)
        {
            OrderDeposit orderDeposit = new OrderDeposit();

            int depositNumber = i + 1;
            ServiceDeposit serviceDeposit =
                    serviceDepositRepository.findServiceDepositsByService(serviceOrder.getAvailableService().getService()).stream().filter(item -> item.getDepositNumber() == depositNumber).findFirst().get();

            float percent = (float) serviceDeposit.getPercent() / 100;
            float totalAmount = serviceOrder.getTotalAmount();

            orderDeposit.setAmount(percent * totalAmount);
            orderDeposit.setDepositNumber(serviceDeposit.getDepositNumber());
            orderDeposit.setPercent(serviceDeposit.getPercent());
            orderDeposit.setServiceOrder(serviceOrder);
            orderDeposit.setCreatedAt(LocalDateTime.now());

            orderDepositRepository.save(orderDeposit);
        }
    }

    @Override
    public Contract customerSignContract(Long serviceOrderId, String customerSign, Long cusId)
    {
        ServiceOrder serviceOrder = serviceRepository.findServiceOrderByOrderId(serviceOrderId);
        serviceOrder.setStatus(ServiceOrderStatusConstant.DA_DUYET_HOP_DONG);

        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setServiceOrder(serviceOrder);
        newOrderProgress.setCreatedTime(LocalDateTime.now());
        newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_DUYET_HOP_DONG);

        Contract contract = contractRepository.findContractByServiceOrder(serviceOrder);
        contract.setCustomerSignature(customerSign);

        User user = userRepository.findUserByUserId(cusId);
        contract.setCusFullName(user.getUsername());
        contract.setCusPhone(user.getPhone());
        contractRepository.save(contract);

        createOrderDeposit(serviceOrder);

        serviceRepository.save(serviceOrder);
        orderProgressRepository.save(newOrderProgress);

        return contract;
    }

    @Override
    public ContractDetailRes getContractByserviceorderId(Long serviceOrderId)
    {
        ServiceOrder serviceOrder = serviceRepository.findServiceOrderByOrderId(serviceOrderId);
        Contract contract = contractRepository.findContractByServiceOrder(serviceOrder);

        if (contract == null) {
            return null;
        }

        User woodworker = userRepository.findUserByUserId(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId());
        List<RequestedProduct> requestedProducts = requestedProductRepository.findRequestedProductByServiceOrder(serviceOrder);

        ContractDetailRes contractDetailRes = modelMapper.map(contract, ContractDetailRes.class);
        contractDetailRes.setCustomer(modelMapper.map(serviceOrder.getUser(), UserDetailRes.class));
        contractDetailRes.setWoodworker(modelMapper.map(woodworker, UserDetailRes.class));
        contractDetailRes.setRequestedProducts(requestedProducts.stream()
                .map(requestedProduct -> modelMapper.map(requestedProduct, RequestedProductListItemRes.class))
                .toList());

        return contractDetailRes;
    }
}
