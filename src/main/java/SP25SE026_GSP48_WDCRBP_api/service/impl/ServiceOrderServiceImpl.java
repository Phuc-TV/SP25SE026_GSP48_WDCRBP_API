package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatus;
import SP25SE026_GSP48_WDCRBP_api.mapper.ServiceOrderMapper;
import SP25SE026_GSP48_WDCRBP_api.model.dto.*;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ShipmentRepository shipmentRepository;

    public ServiceOrderServiceImpl(ServiceOrderRepository orderRepository,
                                   UserRepository userRepository,
                                   AvailableServiceRepository availableServiceRepository,
                                   RequestedProductRepository requestedProductRepository,
                                   OrderProgressRepository orderProgressRepository,
                                   WoodworkerProfileRepository woodworkerProfileRepository,
                                   ConsultantAppointmentRepository consultantAppointmentRepository,
                                   TechSpecRepository techSpecRepository,
                                   CustomerSelectionRepository customerSelectionRepository,
                                   ModelMapper modelMapper,
                                   ShipmentRepository shipmentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.availableServiceRepository = availableServiceRepository;
        this.requestedProductRepository = requestedProductRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
        this.consultantAppointmentRepository = consultantAppointmentRepository;
        this.techSpecRepository = techSpecRepository;
        this.customerSelectionRepository = customerSelectionRepository;
        this.modelMapper = modelMapper;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public List<ServiceOrderDto> listServiceOrderByUserIdOrWwId(Long id, String role) {
        List<ServiceOrder> orders = new ArrayList<>();
        if (role.equals("Customer")) {
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

        List<ServiceOrderDto> serviceOrderDtos = new ArrayList<>();

        for (ServiceOrder serviceOrder : orders)
        {
            AvaliableServiceDto avaliableServiceDto = new AvaliableServiceDto();
            avaliableServiceDto.setService(serviceOrder.getAvailableService().getService());
            wwDto wwDto = new wwDto();
            wwDto.setBrandName(serviceOrder.getAvailableService().getWoodworkerProfile().getBrandName());
            wwDto.setWoodworkerId(serviceOrder.getAvailableService().getWoodworkerProfile().getWoodworkerId());
            wwDto.setAddress(serviceOrder.getAvailableService().getWoodworkerProfile().getAddress());
            wwDto.setBio(serviceOrder.getAvailableService().getWoodworkerProfile().getBio());
            avaliableServiceDto.setWwDto(wwDto);

            serviceOrderDtos.add(ServiceOrderMapper.toDto(serviceOrder, avaliableServiceDto));
        }

        return serviceOrderDtos;
    }

    @Override
    public CoreApiResponse addServiceOrderCustomize(CreateServiceOrderCusRequest createServiceOrderCusRequest) {
        Float totalAmount = 0f;
        List<DesignIdeaVariantCusDto> designIdeaVariantIds = createServiceOrderCusRequest.getDesignIdeaVariantIds();
        short quantity = 0;

        for (DesignIdeaVariantCusDto t: designIdeaVariantIds)
        {
            quantity = (short) (quantity + t.getQuantity());
        }

        if (quantity > 4)
            return CoreApiResponse.error(ServiceOrderStatus.CHI_DUOC_TOI_DA_4_SAN_PHAM);

        //Create ServiceOrder
        User user = userRepository.findById(createServiceOrderCusRequest.getUserId()).orElse(null);

        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderCusRequest.getAvailableServiceId()).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_DUYET);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setQuantity(quantity);
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        for (DesignIdeaVariantCusDto i: designIdeaVariantIds)
        {
            DesignIdeaVariant designIdeaVariant =
                    designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(i.getDesignIdeaVariantId());

            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setDesignIdeaVariant(designIdeaVariant);
            requestedProduct.setServiceOrder(serviceOrder);
            requestedProduct.setTotalAmount(designIdeaVariant.getPrice() * i.getQuantity());
            totalAmount = totalAmount + requestedProduct.getTotalAmount();
            requestedProduct.setCreatedAt(LocalDateTime.now());

            requestedProductRepository.save(requestedProduct);
        }

        serviceOrder.setTotalAmount(totalAmount);
        serviceOrder.setAmountRemaining(totalAmount);
        serviceOrder.setAmountPaid(0.f);
        orderRepository.save(serviceOrder);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());

        orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_DUYET);

        orderProgressRepository.save(orderProgress);

        Shipment shipment = new Shipment();
        shipment.setServiceOrder(serviceOrder);
        shipment.setToAddress(createServiceOrderCusRequest.getAddress());

        shipmentRepository.save(shipment);

        return CoreApiResponse.success(serviceOrder, "successfully");
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

            OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder).getLast();
            orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN);
            orderProgressRepository.save(orderProgress);
        } else if (serviceOrder.getRole().equals("Customer")) {
            serviceOrder.setRole("Customer");
            orderRepository.save(serviceOrder);

            OrderProgress orderProgress = orderProgressRepository.findOrderProgressByServiceOrder(serviceOrder).getLast();
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

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());

        orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_DUYET);

        Shipment shipment = new Shipment();
        shipment.setServiceOrder(serviceOrder);
        shipment.setToAddress(createServiceOrderPersonalizeRequest.getAddress());

        shipmentRepository.save(shipment);

        return newServiceOrder;
    }
}
