package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatus;
import SP25SE026_GSP48_WDCRBP_api.model.dto.RequestedProductPersonalizeDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.TechSpecPersonalizeDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderRequest;
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

    @Autowired
    private TechSpecRepository techSpecRepository;

    @Autowired
    private CustomerSelectionRepository customerSelectionRepository;

    public ServiceOrderServiceImpl(ServiceOrderRepository orderRepository,
                                   UserRepository userRepository,
                                   AvailableServiceRepository availableServiceRepository,
                                   RequestedProductRepository requestedProductRepository,
                                   OrderProgressRepository orderProgressRepository,
                                   WoodworkerProfileRepository woodworkerProfileRepository,
                                   ConsultantAppointmentRepository consultantAppointmentRepository,
                                   TechSpecRepository techSpecRepository,
                                   CustomerSelectionRepository customerSelectionRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.availableServiceRepository = availableServiceRepository;
        this.requestedProductRepository = requestedProductRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
        this.consultantAppointmentRepository = consultantAppointmentRepository;
        this.techSpecRepository = techSpecRepository;
        this.customerSelectionRepository = customerSelectionRepository;
    }

    @Override
    public List<ServiceOrder> listServiceOrderByUserIdOrWwId(Long id, String role) {
        List<ServiceOrder> orders = new ArrayList<>();
        if (role.equals("User")) {
            User user = userRepository.findUserByUserId(id);
            orders = orderRepository.findServiceOrderByUser(user);
        } else {
            if (role.equals("Woodworker")) {
                WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(id);
                List<AvailableService> availableServices =
                        availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworkerProfile);
                for (AvailableService availableService : availableServices) {
                    List<ServiceOrder> serviceOrders = orderRepository.findServiceOrderByAvailableService(availableService);
                    orders.addAll(serviceOrders);
                }
            } else
                return null;
        }

        return orders;
    }

    @Override
    public ServiceOrder addServiceOrderCustomize(CreateServiceOrderRequest createServiceOrderRequest) {
        //Create ServiceOrder
        User user = userRepository.findById(createServiceOrderRequest.getUserId()).orElse(null);

        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderRequest.getAvailableServiceId()).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        //Create RequestedProduct
        int t = orderRepository.findAll().size();
        ServiceOrder newServiceOrder = orderRepository.findAll().get(t - 1);

        DesignIdeaVariant designIdeaVariant =
                designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(createServiceOrderRequest.getDesignIdeaVariantId());

        RequestedProduct requestedProduct = new RequestedProduct();
        requestedProduct.setDesignIdeaVariant(designIdeaVariant);
        requestedProduct.setServiceOrder(newServiceOrder);
        requestedProduct.setTotalAmount(designIdeaVariant.getPrice() * createServiceOrderRequest.getQuantity());
        requestedProduct.setCreatedAt(LocalDateTime.now());

        requestedProductRepository.save(requestedProduct);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);

        orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_DUYET);

        orderProgressRepository.save(orderProgress);

        return serviceOrder;
    }

    @Override
    public ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting) {
        ServiceOrder serviceOrder = orderRepository.findById(serviceOrderId).orElse(null);

        if (serviceOrder.getRole().equals("Woodworker")) {
            ConsultantAppointment consultantAppointment = new ConsultantAppointment();

            if (consultantAppointmentRepository.findConsultantAppointmentByServiceOrder(serviceOrder) != null) {
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

            OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);
            orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN);
            orderProgressRepository.save(orderProgress);
        } else if (serviceOrder.getRole().equals("Customer")) {
            serviceOrder.setRole("Customer");
            orderRepository.save(serviceOrder);

            OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder);
            orderProgress.setStatus(ServiceOrderStatus.DANG_LAM_HOP_DONG);
            orderProgressRepository.save(orderProgress);
        }
        return serviceOrder;
    }

    @Override
    public ServiceOrder customerFeedback(Long serviceOrderId, String feedback) {
        ServiceOrder serviceOrder = orderRepository.findServiceOrderByOrderId(serviceOrderId);

        if (serviceOrder.getRole().equals("Customer")) {
            serviceOrder.setFeedback(feedback);
            serviceOrder.setRole("Woodworker");
            orderRepository.save(serviceOrder);
            return serviceOrder;
        }
        return null;
    }

    @Override
    public ServiceOrder createServiceOrderPersonalize(CreateServiceOrderPersonalizeRequest createServiceOrderPersonalizeRequest)
    {
        //Create ServiceOrder
        User user = userRepository.findById(createServiceOrderPersonalizeRequest.getUserId()).orElse(null);

        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderPersonalizeRequest.getAvailableServiceId()).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        //Create RequestedProduct
        int t = orderRepository.findAll().size();
        ServiceOrder newServiceOrder = orderRepository.findAll().get(t - 1);

        List<RequestedProductPersonalizeDto> requestedProducts =
                createServiceOrderPersonalizeRequest.getRequestedProducts();

        for (RequestedProductPersonalizeDto requestedProductPersonalizeDto : requestedProducts)
        {
            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setQuantity(requestedProductPersonalizeDto.getQuantity());
            requestedProduct.setServiceOrder(newServiceOrder);
            requestedProduct.setCreatedAt(LocalDateTime.now());

            requestedProductRepository.save(requestedProduct);

            List<TechSpecPersonalizeDto> techSpecs = requestedProductPersonalizeDto.getTechSpecs();

            for (TechSpecPersonalizeDto techSpecDto : techSpecs)
            {
                CustomerSelection customerSelection = new CustomerSelection();
                customerSelection.setTechSpec(techSpecRepository.findById(techSpecDto.getTechSpecId()).orElse(null));
                customerSelection.setValue(techSpecDto.getValues());
                customerSelection.setRequestedProduct(requestedProduct);
                customerSelection.setCreatedAt(LocalDateTime.now());

                customerSelectionRepository.save(customerSelection);
            }
        }

        return newServiceOrder;
    }
}
