package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatus;
import SP25SE026_GSP48_WDCRBP_api.mapper.DesignIdeaVariantMapper;
import SP25SE026_GSP48_WDCRBP_api.mapper.ServiceOrderMapper;
import SP25SE026_GSP48_WDCRBP_api.model.dto.*;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
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
    private DesignIdeaVariantConfigRepository designIdeaVariantConfigRepository;

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

    @Autowired
    private ProductImagesRepository productImagesRepository;

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

        for (ServiceOrder serviceOrder : orders) {
            AvaliableServiceDto avaliableServiceDto = new AvaliableServiceDto();
            avaliableServiceDto.setService(serviceOrder.getAvailableService().getService());

            wwDto wwDto = new wwDto();
            wwDto.setBrandName(serviceOrder.getAvailableService().getWoodworkerProfile().getBrandName());
            wwDto.setWoodworkerId(serviceOrder.getAvailableService().getWoodworkerProfile().getWoodworkerId());
            wwDto.setAddress(serviceOrder.getAvailableService().getWoodworkerProfile().getAddress());
            wwDto.setBio(serviceOrder.getAvailableService().getWoodworkerProfile().getBio());
            avaliableServiceDto.setWwDto(wwDto);

            UserDetailRes userDetailRes = modelMapper.map(serviceOrder.getUser(), UserDetailRes.class);

            serviceOrderDtos.add(ServiceOrderMapper.toServiceOrderDto(serviceOrder, avaliableServiceDto, userDetailRes));
        }

        return serviceOrderDtos;
    }

    @Override
    public ServiceOrderDetailRes getServiceDetailById(Long id) {
        ServiceOrder order = orderRepository.findServiceOrderByOrderId(id);

        if (order == null) {
            return null;
        }

        // Build service order dto
        AvaliableServiceDto avaliableServiceDto = new AvaliableServiceDto();
        avaliableServiceDto.setService(order.getAvailableService().getService());

        wwDto wwDto = new wwDto();
        wwDto.setBrandName(order.getAvailableService().getWoodworkerProfile().getBrandName());
        wwDto.setWoodworkerId(order.getAvailableService().getWoodworkerProfile().getWoodworkerId());
        wwDto.setAddress(order.getAvailableService().getWoodworkerProfile().getAddress());
        wwDto.setBio(order.getAvailableService().getWoodworkerProfile().getBio());
        avaliableServiceDto.setWwDto(wwDto);

        UserDetailRes userDetailRes = modelMapper.map(order.getUser(), UserDetailRes.class);

        ServiceOrderDto serviceOrderDto = ServiceOrderMapper.toServiceOrderDto(order, avaliableServiceDto, userDetailRes);

        // Build ConsultantAppointmentDetailRes
        ConsultantAppointmentDetailRes consultantAppointmentDetailRes = order.getConsultantAppointment() != null ? modelMapper.map(order.getConsultantAppointment(), ConsultantAppointmentDetailRes.class) : null;

        // Build ReviewRes
        ReviewRes reviewRes = order.getReview() != null ? modelMapper.map(order.getReview(), ReviewRes.class) : null;

        // Build RequestedProductDetailRes
        List<RequestedProduct> requestedProducts = requestedProductRepository.findByServiceOrder(order);
        List<RequestedProductDetailRes> requestedProductDetailResList = new ArrayList<>();

        for (RequestedProduct requestedProduct : requestedProducts) {
            // Customization
            if (requestedProduct.getDesignIdeaVariant() != null) {
                // Design idea
                DesignIdea idea = requestedProduct.getDesignIdeaVariant().getDesignIdea();
                // Design idea variant
                DesignIdeaVariant variant = designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(requestedProduct.getDesignIdeaVariant().getDesignIdeaVariantId());
                // Design idea variant config
                List<DesignIdeaVariantConfig> configs =
                        designIdeaVariantConfigRepository.findDesignIdeaVariantConfigByDesignIdeaVariant(variant);
                // Design idea detail res
                DesignVariantDetailRes designIdeaDetailRes = DesignIdeaVariantMapper.toDto(variant, configs, idea);
                // RequestedProduct for Customize detail
                RequestedProductDetailRes requestedProductDetailRes = new RequestedProductDetailRes();

                requestedProductDetailRes.setRequestedProductId(requestedProduct.getRequestedProductId());
                requestedProductDetailRes.setQuantity(requestedProduct.getQuantity());
                requestedProductDetailRes.setTotalAmount(requestedProduct.getTotalAmount());
                requestedProductDetailRes.setCreatedAt(requestedProduct.getCreatedAt());
                requestedProductDetailRes.setHasDesign(requestedProduct.getDesignIdeaVariant() != null);
                requestedProductDetailRes.setDesignIdeaVariantDetail(designIdeaDetailRes);

                requestedProductDetailResList.add(requestedProductDetailRes);
            } else {
                // Personalization
            }
        }

        return ServiceOrderMapper.toServiceOrderDetailRes(serviceOrderDto, requestedProductDetailResList, consultantAppointmentDetailRes, reviewRes);
    }

    @Override
    public CoreApiResponse addServiceOrderCustomize(CreateServiceOrderCusRequest createServiceOrderCusRequest) {
        Float totalAmount = 0f;
        List<DesignIdeaVariantCusDto> designIdeaVariantIds = createServiceOrderCusRequest.getDesignIdeaVariantIds();
        short quantity = 0;

        for (DesignIdeaVariantCusDto t : designIdeaVariantIds) {
            quantity = (short) (quantity + t.getQuantity());
        }

        if (quantity > 4)
            return CoreApiResponse.error(ServiceOrderStatus.CHI_DUOC_TOI_DA_4_SAN_PHAM);

        //Create ServiceOrder
        User user = userRepository.findById(createServiceOrderCusRequest.getUserId()).orElse(null);

        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderCusRequest.getAvailableServiceId()).orElse(null);

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setDescription(createServiceOrderCusRequest.getDescription());
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_XAC_NHAN);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setQuantity(quantity);
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        for (DesignIdeaVariantCusDto i : designIdeaVariantIds) {
            DesignIdeaVariant designIdeaVariant =
                    designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(i.getDesignIdeaVariantId());

            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setDesignIdeaVariant(designIdeaVariant);
            requestedProduct.setQuantity(Byte.parseByte(i.getQuantity() + ""));
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

        orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_XAC_NHAN);

        orderProgressRepository.save(orderProgress);

        Shipment shipment = new Shipment();
        shipment.setServiceOrder(serviceOrder);
        shipment.setToAddress(createServiceOrderCusRequest.getAddress());

        shipmentRepository.save(shipment);

        return CoreApiResponse.success(serviceOrder, "successfully");
    }

    @Override
    public ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting, String form, String desc) {
        ServiceOrder serviceOrder = orderRepository.findById(serviceOrderId).orElse(null);
        String currentStatus = serviceOrder.getStatus();

        if (serviceOrder == null) {
            return null;
        }

        // Create new progress record with next status based on current status and role
        OrderProgress newOrderProgress = new OrderProgress();
        OrderProgress continueOrderProgress = new OrderProgress();
        newOrderProgress.setServiceOrder(serviceOrder);
        newOrderProgress.setCreatedTime(LocalDateTime.now());

        switch (serviceOrder.getRole()) {
            case "Woodworker":
                if (currentStatus == null || currentStatus.equals(ServiceOrderStatus.DANG_CHO_THO_MOC_XAC_NHAN) || currentStatus.equals(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    // Woodworker accepting initial order, setting appointment
                    ConsultantAppointment consultantAppointment = new ConsultantAppointment();
                    if (consultantAppointmentRepository.findConsultantAppointmentByServiceOrder(serviceOrder) != null) {
                        consultantAppointment = consultantAppointmentRepository.findConsultantAppointmentByServiceOrder(serviceOrder);
                    }

                    consultantAppointment.setServiceOrder(serviceOrder);
                    consultantAppointment.setCreatedAt(LocalDateTime.now());
                    consultantAppointment.setDateTime(timeMeeting);
                    consultantAppointment.setMeetAddress(linkMeeting);
                    consultantAppointment.setContent(desc);
                    consultantAppointment.setForm(form);
                    consultantAppointmentRepository.save(consultantAppointment);

                    serviceOrder.setConsultantAppointment(consultantAppointment);
                    serviceOrder.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN);
                    newOrderProgress.setStatus(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN);
                }

                break;
            case "Customer":
                if (currentStatus.equals(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    // Customer approving appointment
                    newOrderProgress.setStatus(ServiceOrderStatus.DA_DUYET_LICH_HEN);
                    serviceOrder.setStatus(ServiceOrderStatus.DA_DUYET_LICH_HEN);
                } else if (currentStatus.equals(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_HOP_DONG)) {
                    // Customer approving contract
                    newOrderProgress.setStatus(ServiceOrderStatus.DA_DUYET_HOP_DONG);
                    serviceOrder.setStatus(ServiceOrderStatus.DA_DUYET_HOP_DONG);

                    if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.CUSTOMIZATION)) {
                        continueOrderProgress.setServiceOrder(serviceOrder);
                        continueOrderProgress.setCreatedTime(LocalDateTime.now());
                        continueOrderProgress.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                        serviceOrder.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                    }
                } else if (currentStatus.equals(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_THIET_KE)) {
                    // Customer apporoving design
                    newOrderProgress.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                    serviceOrder.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                }

                break;
        }

        if (currentStatus.equals(ServiceOrderStatus.DANG_CHO_KHACH_DUYET_LICH_HEN) && serviceOrder.getRole().equals("Woodworker")) {
            // Empty
        } else {
            orderProgressRepository.save(newOrderProgress);
        }

        if (continueOrderProgress.getStatus() != null) {
            orderProgressRepository.save(continueOrderProgress);
        }

        // Toggle the role between Woodworker and Customer
        serviceOrder.setRole(serviceOrder.getRole().equals("Woodworker") ? "Customer" : "Woodworker");

        // Clear feedback when moving to next stage
        serviceOrder.setFeedback(null);

        // Save changes
        orderRepository.save(serviceOrder);

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
    public ServiceOrder createServiceOrderPersonalize(CreateServiceOrderPersonalizeRequest createServiceOrderPersonalizeRequest) {
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

        List<RequestedProductPersonalizeDto> requestedProducts =
                createServiceOrderPersonalizeRequest.getRequestedProducts();

        for (RequestedProductPersonalizeDto requestedProductPersonalizeDto : requestedProducts) {
            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setQuantity(requestedProductPersonalizeDto.getQuantity());
            requestedProduct.setServiceOrder(serviceOrder);
            requestedProduct.setCreatedAt(LocalDateTime.now());

            requestedProductRepository.save(requestedProduct);

            List<TechSpecPersonalizeDto> techSpecs = requestedProductPersonalizeDto.getTechSpecs();

            for (TechSpecPersonalizeDto techSpecDto : techSpecs) {
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

        orderProgress.setStatus(ServiceOrderStatus.DANG_CHO_THO_MOC_XAC_NHAN);

        Shipment shipment = new Shipment();
        shipment.setServiceOrder(serviceOrder);
        shipment.setToAddress(createServiceOrderPersonalizeRequest.getAddress());

        shipmentRepository.save(shipment);

        return serviceOrder;
    }

    @Override
    public List<ProductImages> addProductImage(List<ProductImagesDto> productImagesDtos, Long serviceId)
    {
        ServiceOrder serviceOrder = orderRepository.findById(serviceId).orElse(null);

        serviceOrder.setRole("Woodworker");
        serviceOrder.setFeedback(null);

        orderRepository.save(serviceOrder);

        List<ProductImages> productImages = new ArrayList<>();
        for (ProductImagesDto productImagesDto : productImagesDtos)
        {
            RequestedProduct requestedProduct =
                    requestedProductRepository.findRequestedProductByRequestedProductId(productImagesDto.getProductId());
            ProductImages productImage = new ProductImages();
            productImage.setRequestedProduct(requestedProduct);
            productImage.setMediaUrls(productImagesDto.getMediaUrls());
            productImagesRepository.save(productImage);

            productImages.add(productImage);
        }
        return productImages;
    }
}