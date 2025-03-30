package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.AddServiceOrderRequest;
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
    public ServiceOrder addServiceOrderCustomize(AddServiceOrderRequest addServiceOrderRequest)
    {
        //Create ServiceOrder
        User user = userRepository.findById(addServiceOrderRequest.getUserId()).orElse(null);

        AvailableService availableService =
                availableServiceRepository.findById(addServiceOrderRequest.getAvailableServiceId()).orElse(null);

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
                designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(addServiceOrderRequest.getDesignIdeaVariantId());

        RequestedProduct requestedProduct = new RequestedProduct();
        requestedProduct.setDesignIdeaVariant(designIdeaVariant);
        requestedProduct.setServiceOrder(newServiceOrder);
        requestedProduct.setTotalAmount(designIdeaVariant.getPrice() * addServiceOrderRequest.getQuantity());
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

            if (consultantAppointmentRepository.findConsultantAppointmentByServiceOrder(serviceOrder) != null)
            {
                consultantAppointment =
                        consultantAppointmentRepository.findConsultantAppointmentByServiceOrder(serviceOrder);
            }

            consultantAppointment.setServiceOrder(serviceOrder);
            consultantAppointment.setCreatedAt(LocalDateTime.now());
            consultantAppointment.setDateTime(timeMeeting);
            consultantAppointment.setMeetAddress(linkMeeting);

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

    @Override
    public ServiceOrder customerFeedback(Long serviceOrderId, String feedback)
    {
        ServiceOrder serviceOrder = orderRepository.findServiceOrderByOrderId(serviceOrderId);

        if (serviceOrder.getRole().equals("Customer"))
        {
            serviceOrder.setFeedback(feedback);
            serviceOrder.setRole("Woodworker");
            orderRepository.save(serviceOrder);
            return serviceOrder;
        }
        return null;
    }
}
