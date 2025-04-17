package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.GuaranteeOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderProgress;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.repository.GuaranteeOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.OrderProgressRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.service.OrderProgressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProgressServiceImpl implements OrderProgressService {
    @Autowired
    private OrderProgressRepository orderProgressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;
    @Autowired
    private GuaranteeOrderRepository guaranteeOrderRepository;

    public OrderProgressServiceImpl(OrderProgressRepository orderProgressRepository,
                                    ModelMapper modelMapper,
                                    ServiceOrderRepository serviceOrderRepository)
    {
        this.orderProgressRepository = orderProgressRepository;
        this.modelMapper = modelMapper;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Override
    public List<OrderProgress> getAllOrderProgressByOrderId(Long orderId)
    {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(orderId);
        List<OrderProgress> orderProgresses = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);

        return orderProgresses;
    }

    @Override
    public List<OrderProgress> getAllOrderProgressByGuaranteeId(Long orderId) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(orderId);
        List<OrderProgress> orderProgresses = orderProgressRepository.findOrderProgressByGuaranteeOrder(guaranteeOrder);

        return orderProgresses;
    }
}
