package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.repository.OrderDepositRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.service.OrderDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDepositServiceImpl implements OrderDepositService {
    @Autowired
    private OrderDepositRepository orderDepositRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public OrderDepositServiceImpl(OrderDepositRepository orderDepositRepository,
                                   ServiceOrderRepository serviceOrderRepository)
    {
        this.orderDepositRepository = orderDepositRepository;
    }

    @Override
    public List<OrderDeposit> getAllOrderDepositByOrderId(Long orderId)
    {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(orderId);

        List<OrderDeposit> orderDeposits = orderDepositRepository.findOrderDepositByServiceOrder(serviceOrder);

        return orderDeposits;
    }
}
