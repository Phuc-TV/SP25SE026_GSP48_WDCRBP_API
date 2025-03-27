package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {
    @Autowired
    private ServiceOrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestedProductRepository requestedProductRepository;

    @Autowired
    private DesignIdeaVariantRepository designIdeaVariantRepository;

    @Autowired
    private AvailableServiceRepository availableServiceRepository;

    public ServiceOrderServiceImpl (ServiceOrderRepository orderRepository,
                                    UserRepository userRepository,
                                    AvailableServiceRepository availableServiceRepository,
                                    RequestedProductRepository requestedProductRepository)
    {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.availableServiceRepository = availableServiceRepository;
        this.requestedProductRepository = requestedProductRepository;
    }

    public ServiceOrder addServiceOrderCustomize(Long availableServiceId, Long userId, Long designIdeaVariantId)
    {
        //Create ServiceOrder
        User user = userRepository.findById(userId).orElse(null);

        AvailableService availableService = availableServiceRepository.findById(availableServiceId).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setCreatedAt(LocalDateTime.now());

        orderRepository.save(serviceOrder);

        //Create RequestedProduct
        int t = orderRepository.findAll().size();
        ServiceOrder newServiceOrder = orderRepository.findAll().get(t-1);

        DesignIdeaVariant designIdeaVariant =
                designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(designIdeaVariantId);

        RequestedProduct requestedProduct = new RequestedProduct();
        requestedProduct.setDesignIdeaVariant(designIdeaVariant);
        requestedProduct.setServiceOrder(newServiceOrder);
        requestedProduct.setTotalAmount(designIdeaVariant.getPrice());
        requestedProduct.setCreatedAt(LocalDateTime.now());

        requestedProductRepository.save(requestedProduct);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setStatus("Đang chờ thợ mộc duyệt");

        return serviceOrder;
    }
}
