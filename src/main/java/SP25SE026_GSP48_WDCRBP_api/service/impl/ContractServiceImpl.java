package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ContractCustomizeRequest;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ContractService;
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

    public ContractServiceImpl(ContractRepository contractRepository,
                               ServiceOrderRepository serviceRepository,
                               UserRepository userRepository,
                               OrderDepositRepository orDepositRepository,
                               OrderProgressRepository orderProgressRepository)
    {
        this.contractRepository = contractRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.orderDepositRepository = orDepositRepository;
    }

    @Override
    public Contract createContractCustomize(ContractCustomizeRequest contractCustomizeRequest)
    {
        Contract contract = new Contract();
        contract.setWarrantyPeriod(contractCustomizeRequest.getWarrantyPeriod());
        contract.setWarrantyPolicy(contractCustomizeRequest.getWarrantyPolicy());
        contract.setCompleteDate(contractCustomizeRequest.getCompleteDate());
        contract.setSignDate(LocalDateTime.now());

        ServiceOrder serviceOrder =
                serviceRepository.findServiceOrderByOrderId(contractCustomizeRequest.getServiceOrderId());

        serviceOrder.setTotalAmount(contractCustomizeRequest.getTotalAmount());
        serviceRepository.save(serviceOrder);

        contract.setServiceOrder(serviceOrder);
        contractRepository.save(contract);

        serviceRepository.save(serviceOrder);

        return contract;
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

        OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);
        String s = "Đang chờ thanh toán";
        orderProgress.setStatus(s);
        orderProgressRepository.save(orderProgress);

        return contract;
    }
}
