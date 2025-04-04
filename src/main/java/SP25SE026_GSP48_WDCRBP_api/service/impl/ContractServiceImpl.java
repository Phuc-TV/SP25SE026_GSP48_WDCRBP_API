package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatus;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WwCreateContractCustomizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ContractDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ContractService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

        if (serviceOrder.getStatus().equals(ServiceOrderStatus.DA_DUYET_LICH_HEN)) {
            OrderProgress newOrderProgress = new OrderProgress();
            newOrderProgress.setServiceOrder(serviceOrder);
            newOrderProgress.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_HOP_DONG);
            orderProgressRepository.save(newOrderProgress);
        }

        Contract contract1 = contractRepository.findContractByServiceOrder(serviceOrder);

        if (contract1 == null)
        {
            Contract contract = new Contract();
            contract.setWarrantyPeriod(wwCreateContractCustomizeRequest.getWarrantyPeriod());
            contract.setWarrantyPolicy(wwCreateContractCustomizeRequest.getWarrantyPolicy());
            contract.setCompleteDate(wwCreateContractCustomizeRequest.getCompleteDate());
            contract.setWoodworkerSignature(wwCreateContractCustomizeRequest.getWoodworkerSignature());
            contract.setSignDate(LocalDateTime.now());

            List<RequestedProduct> requestedProduct =
                    requestedProductRepository.findRequestedProductByServiceOrder(serviceOrder);

            float i =0;
            for (RequestedProduct rp : requestedProduct)
            {
                i = i+ rp.getTotalAmount();
            }

            serviceOrder.setFeedback("");
            serviceOrder.setTotalAmount(i);
            serviceOrder.setRole("Customer");
            serviceOrder.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_HOP_DONG);
            serviceRepository.save(serviceOrder);

            contract.setContractTotalAmount(i);
            contract.setServiceOrder(serviceOrder);
            contractRepository.save(contract);

            serviceRepository.save(serviceOrder);
            return contract;
        }
        else
        {
            if (wwCreateContractCustomizeRequest.getWarrantyPolicy() != null)
                contract1.setWarrantyPolicy(wwCreateContractCustomizeRequest.getWarrantyPolicy());

            if (wwCreateContractCustomizeRequest.getCompleteDate() != null)
                contract1.setCompleteDate(wwCreateContractCustomizeRequest.getCompleteDate());

            if (wwCreateContractCustomizeRequest.getWarrantyPeriod() != null)
                contract1.setWarrantyPeriod(wwCreateContractCustomizeRequest.getWarrantyPeriod());

            contractRepository.save(contract1);

            serviceOrder.setFeedback("");
            serviceOrder.setRole("Customer");
            serviceRepository.save(serviceOrder);

            return contract1;
        }
    }

    @Override
    public Contract customerSignContract(Long serviceOrderId, String customerSign, Long cusId)
    {
        ServiceOrder serviceOrder = serviceRepository.findServiceOrderByOrderId(serviceOrderId);

        Contract contract = contractRepository.findContractByServiceOrder(serviceOrder);
        contract.setCustomerSignature(customerSign);

        User user = userRepository.findUserByUserId(cusId);
        contract.setCusFullName(user.getUsername());
        contract.setCusPhone(user.getPhone());
        contractRepository.save(contract);

        Short numberOfDeposits = serviceOrder.getAvailableService().getService().getNumberOfDeposits();

        for (int i = 0; i < numberOfDeposits; i++)
        {
            OrderDeposit orderDeposit = new OrderDeposit();

            List<ServiceDeposit> serviceDeposit =
                    serviceDepositRepository.findServiceDepositsByService(serviceOrder.getAvailableService().getService());

            orderDeposit.setAmount(serviceOrder.getTotalAmount() * (serviceDeposit.get(i).getPercent()/100));
            orderDeposit.setPercent(serviceDeposit.get(i).getPercent());
            orderDeposit.setServiceOrder(serviceOrder);
            orderDeposit.setCreatedAt(LocalDateTime.now());

            orderDepositRepository.save(orderDeposit);
        }

        OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder).
                get(orderProgressRepository.findAll().size()-1);
        String s = "Đang chờ thanh toán";
        orderProgress.setStatus(s);
        orderProgressRepository.save(orderProgress);

        return contract;
    }

    @Override
    public ContractDetailRes getContractByserviceorderId(Long serviceOrderId)
    {
        ServiceOrder serviceOrder = serviceRepository.findServiceOrderByOrderId(serviceOrderId);
        Contract contract = contractRepository.findContractByServiceOrder(serviceOrder);
        User woodworker = userRepository.findUserByUserId(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId());

        ContractDetailRes contractDetailRes = modelMapper.map(contract, ContractDetailRes.class);
        contractDetailRes.setCustomer(modelMapper.map(serviceOrder.getUser(), UserDetailRes.class));
        contractDetailRes.setWoodworker(modelMapper.map(woodworker, UserDetailRes.class));

        return contractDetailRes;
    }
}
