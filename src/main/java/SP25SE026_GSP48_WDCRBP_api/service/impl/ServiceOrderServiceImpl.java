package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {
    @Autowired
    private ServiceOrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WoodworkerProfileRepository woodworkerProfileRepository;

    @Autowired
    private RequestedProductRepository requestedProductRepository;

    @Autowired
    private DesignIdeaVariantRepository designIdeaVariantRepository;

    @Autowired
    private AvailableServiceRepository availableServiceRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;

    @Autowired
    private ConsultantAppointmentRepository consultantAppointmentRepository;

    public ServiceOrderServiceImpl (ServiceOrderRepository orderRepository,
                                    UserRepository userRepository,
                                    AvailableServiceRepository availableServiceRepository,
                                    RequestedProductRepository requestedProductRepository,
                                    OrderProgressRepository orderProgressRepository,
                                    WoodworkerProfileRepository woodworkerProfileRepository,
                                    ConsultantAppointmentRepository consultantAppointmentRepository)
    {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.availableServiceRepository = availableServiceRepository;
        this.requestedProductRepository = requestedProductRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
        this.consultantAppointmentRepository = consultantAppointmentRepository;
    }

    @Override
    public List<ServiceOrder> listServiceOrderByUserIdOrWwId(Long id, String role)
    {
        List<ServiceOrder> orders = new ArrayList<>();
        if (role.equals("User"))
        {
            User user = userRepository.findUserByUserId(id);
             orders = orderRepository.findServiceOrderByUser(user);
        }
        else
        {
            if (role.equals("Woodworker"))
            {
                WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(id);
                List<AvailableService> availableServices =
                        availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworkerProfile);
                for (AvailableService availableService : availableServices)
                {
                    List<ServiceOrder> serviceOrders = orderRepository.findServiceOrderByAvailableService(availableService);
                    orders.addAll(serviceOrders);
                }
            }
            else
                return null;
        }

        return orders;
    }

    @Override
    public ServiceOrder addServiceOrderCustomize(Long availableServiceId, Long userId, Long designIdeaVariantId)
    {
        //Create ServiceOrder
        User user = userRepository.findById(userId).orElse(null);

        AvailableService availableService = availableServiceRepository.findById(availableServiceId).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setRole("Woodworker");

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

        String s = "Đang chờ thợ mộc duyệt";
        orderProgress.setStatus(s);

        orderProgressRepository.save(orderProgress);

        return serviceOrder;
    }

    @Override
    public ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting)
    {
        ServiceOrder serviceOrder = orderRepository.findById(serviceOrderId).orElse(null);

        if (serviceOrder.getRole().equals("Woodworker"))
        {
            ConsultantAppointment consultantAppointment = new ConsultantAppointment();
            consultantAppointment.setServiceOrder(serviceOrder);
            consultantAppointment.setCreatedAt(LocalDateTime.now());
            consultantAppointment.setDateTime(timeMeeting);
            consultantAppointment.setMeetAddress(linkMeeting);
            consultantAppointment.setMeetAddress("https://meet.google.com/qta-thit-eaj");

            consultantAppointmentRepository.save(consultantAppointment);

            serviceOrder.setConsultantAppointment(consultantAppointment);
            serviceOrder.setRole("Customer");

            orderRepository.save(serviceOrder);

            String s = "Đang chờ khách hàng duyệt lịch hẹn";
            OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);
            orderProgress.setStatus(s);
            orderProgressRepository.save(orderProgress);
        }
        else if (serviceOrder.getRole().equals("Customer"))
        {
                serviceOrder.setRole("Customer");
                orderRepository.save(serviceOrder);

                String s = "Đang làm hợp đồng";
                OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);
                orderProgress.setStatus(s);
                orderProgressRepository.save(orderProgress);
        }
        return serviceOrder;
    }
        else
            return null;
        return serviceOrder;
    }
}
