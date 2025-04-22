package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.mapper.DesignIdeaVariantMapper;
import SP25SE026_GSP48_WDCRBP_api.mapper.ServiceOrderMapper;
import SP25SE026_GSP48_WDCRBP_api.model.dto.*;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderSaleRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ContractService;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private CategoryRepository categoryRepository;

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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ContractServiceImpl contractServiceImpl;

    @Autowired
    private ContractRepository contractRepository;

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
            wwDto.setPhone(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getPhone());
            wwDto.setName(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getUsername());
            avaliableServiceDto.setWwDto(wwDto);

            UserDetailRes userDetailRes = modelMapper.map(serviceOrder.getUser(), UserDetailRes.class);

            serviceOrderDtos.add(ServiceOrderMapper.toServiceOrderDto(serviceOrder, avaliableServiceDto, userDetailRes));
        }

        return serviceOrderDtos;
    }

    @Override
    public ServiceOrderDetailRes getServiceDetailById(Long id) {
        ServiceOrder order = orderRepository.findServiceOrderByOrderId(id);
        Contract contract = contractRepository.findContractByServiceOrder(order);

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
        wwDto.setPhone(order.getAvailableService().getWoodworkerProfile().getUser().getPhone());
        wwDto.setName(order.getAvailableService().getWoodworkerProfile().getUser().getUsername());
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
            RequestedProductDetailRes requestedProductDetailRes = new RequestedProductDetailRes();

            requestedProductDetailRes.setRequestedProductId(requestedProduct.getRequestedProductId());
            requestedProductDetailRes.setQuantity(requestedProduct.getQuantity());
            requestedProductDetailRes.setWarrantyDuration(requestedProduct.getWarrantyDuration());
            requestedProductDetailRes.setTotalAmount(requestedProduct.getTotalAmount());
            requestedProductDetailRes.setCategory(requestedProduct.getCategory());
            requestedProductDetailRes.setCreatedAt(requestedProduct.getCreatedAt());
            ProductImages finishImages =
                    productImagesRepository.findByRequestedProduct(requestedProduct).stream().filter(design -> design.getType().equals("finish")).findFirst().orElse(null);
            if (finishImages != null)
            {
                requestedProductDetailRes.setFinishImgUrls(finishImages.getMediaUrls());
            }

            if (requestedProduct.getDesignIdeaVariant() != null) {
                DesignIdea idea = requestedProduct.getDesignIdeaVariant().getDesignIdea();
                DesignIdeaVariant variant = designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(requestedProduct.getDesignIdeaVariant().getDesignIdeaVariantId());
                List<DesignIdeaVariantConfig> configs =
                        designIdeaVariantConfigRepository.findDesignIdeaVariantConfigByDesignIdeaVariant(variant);
                DesignVariantDetailRes designIdeaDetailRes = DesignIdeaVariantMapper.toDto(variant, configs, idea);

                requestedProductDetailRes.setDesignIdeaVariantDetail(designIdeaDetailRes);
            } else if (requestedProduct.getProduct() != null) {
                ProductListItemRes productListItemRes = modelMapper.map(requestedProduct.getProduct(), ProductListItemRes.class);

                requestedProductDetailRes.setProduct(productListItemRes);
            } else {
                PersonalProductDetailRes personalProductDetailRes = new PersonalProductDetailRes();
                List<CustomerSelection> customerSelections =
                        customerSelectionRepository.findByRequestedProduct(requestedProduct);
                ProductImages productImages =
                        productImagesRepository.findByRequestedProduct(requestedProduct).stream().filter(design -> design.getType().equals("design")).findFirst().orElse(null);
                if (productImages != null)
                {
                    personalProductDetailRes.setDesignUrls(productImages.getMediaUrls());
                }

                List<TechSpecDetailRes> techSpecDetailResList = new ArrayList<>();
                for (CustomerSelection customerSelection : customerSelections) {
                    TechSpec techSpec = customerSelection.getTechSpec();
                    if (techSpec != null) {
                        TechSpecDetailRes techSpecDetailRes = new TechSpecDetailRes();
                        techSpecDetailRes.setName(techSpec.getName());
                        techSpecDetailRes.setValue(customerSelection.getValue());
                        techSpecDetailRes.setOptionType(techSpec.getOptionType());
                        techSpecDetailResList.add(techSpecDetailRes);
                    }
                }

                personalProductDetailRes.setTechSpecList(techSpecDetailResList);
                requestedProductDetailRes.setPersonalProductDetail(personalProductDetailRes);
            }

            requestedProductDetailResList.add(requestedProductDetailRes);
        }

        ServiceOrderDetailRes serviceOrderDetailRes = ServiceOrderMapper.toServiceOrderDetailRes(serviceOrderDto, requestedProductDetailResList, consultantAppointmentDetailRes, reviewRes);
        serviceOrderDetailRes.setCompleteDate(contract != null ? contract.getCompleteDate() : null);

        return serviceOrderDetailRes;
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
            return CoreApiResponse.error(ServiceOrderStatusConstant.CHI_DUOC_TOI_DA_4_SAN_PHAM);

        //Create ServiceOrder
        User user = userRepository.findById(createServiceOrderCusRequest.getUserId()).orElse(null);
        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderCusRequest.getAvailableServiceId()).orElse(null);
        WoodworkerProfile ww = availableService.getWoodworkerProfile();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setDescription(createServiceOrderCusRequest.getDescription());
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setQuantity(quantity);
        serviceOrder.setInstall(createServiceOrderCusRequest.getIsInstall());
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        for (DesignIdeaVariantCusDto i : designIdeaVariantIds) {
            DesignIdeaVariant designIdeaVariant =
                    designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(i.getDesignIdeaVariantId());

            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setDesignIdeaVariant(designIdeaVariant);
            requestedProduct.setQuantity(Byte.parseByte(i.getQuantity() + ""));
            requestedProduct.setServiceOrder(serviceOrder);
            requestedProduct.setCategory(designIdeaVariant.getDesignIdea().getCategory());
            requestedProduct.setTotalAmount(designIdeaVariant.getPrice() * i.getQuantity());
            totalAmount = totalAmount + requestedProduct.getTotalAmount();
            requestedProduct.setCreatedAt(LocalDateTime.now());

            requestedProductRepository.save(requestedProduct);
        }

        serviceOrder.setTotalAmount(totalAmount + createServiceOrderCusRequest.getPriceShipping());
        serviceOrder.setAmountRemaining(totalAmount + createServiceOrderCusRequest.getPriceShipping());
        serviceOrder.setShipFee((float)createServiceOrderCusRequest.getPriceShipping());
        serviceOrder.setAmountPaid(0.f);
        orderRepository.save(serviceOrder);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());

        orderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);

        orderProgressRepository.save(orderProgress);

        if (createServiceOrderCusRequest.getIsInstall()) {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(createServiceOrderCusRequest.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit(availableService.getWoodworkerProfile().getBrandName());
            shipment.setShipType("Giao hàng và lắp đặt bởi xưởng mộc");
            shipmentRepository.save(shipment);
        } else {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(createServiceOrderCusRequest.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit("Giao hàng nhanh (GHN)");
            shipment.setShipType("Giao hàng bởi bên thứ 3 (GHN)");
            shipment.setToDistrictId(Integer.parseInt(createServiceOrderCusRequest.getToDistrictId()));
            shipment.setToWardCode(createServiceOrderCusRequest.getToWardCode());
            shipment.setFromDistrictId(Integer.parseInt(ww.getDistrictId()));
            shipment.setFromWardCode(ww.getWardCode());
            shipment.setFee(createServiceOrderCusRequest.getPriceShipping());
            shipment.setGhnServiceId(createServiceOrderCusRequest.getGhnServiceId());
            shipment.setGhnServiceTypeId(createServiceOrderCusRequest.getGhnServiceTypeId());
            shipmentRepository.save(shipment);
        }

        return CoreApiResponse.success(serviceOrder, "successfully");
    }

    @Override
    public void addSaleOrder(CreateServiceOrderSaleRequest request) {
        Float totalAmount = 0f;
        List<ProductSaleDto> productIds = request.getProductIds();
        short quantity = 0;

        for (ProductSaleDto t : productIds) {
            quantity = (short) (quantity + t.getQuantity());
        }

        //Create ServiceOrder
        User user = userRepository.findById(request.getUserId()).orElse(null);
        AvailableService availableService =
                availableServiceRepository.findById(request.getAvailableServiceId()).orElse(null);
        WoodworkerProfile ww = availableService.getWoodworkerProfile();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setDescription(request.getDescription());
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setUser(user);
        serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setQuantity(quantity);
        serviceOrder.setInstall(request.getIsInstall());
        serviceOrder.setRole("Woodworker");

        orderRepository.save(serviceOrder);

        for (ProductSaleDto i : productIds) {
            Product product =
                    productRepository.findProductByProductId(i.getProductId());

            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setProduct(product);
            requestedProduct.setQuantity(Byte.parseByte(i.getQuantity() + ""));
            requestedProduct.setServiceOrder(serviceOrder);
            requestedProduct.setCategory(product.getCategory());
            requestedProduct.setTotalAmount(product.getPrice() * i.getQuantity());
            requestedProduct.setWarrantyDuration(product.getWarrantyDuration());
            totalAmount = totalAmount + requestedProduct.getTotalAmount();
            requestedProduct.setCreatedAt(LocalDateTime.now());

            requestedProductRepository.save(requestedProduct);
        }

        serviceOrder.setTotalAmount(totalAmount + request.getPriceShipping());
        serviceOrder.setAmountRemaining(totalAmount + request.getPriceShipping());
        serviceOrder.setShipFee((float)request.getPriceShipping());
        serviceOrder.setAmountPaid(0.f);
        orderRepository.save(serviceOrder);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());

        orderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);

        orderProgressRepository.save(orderProgress);

        if (request.getIsInstall()) {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(request.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit(availableService.getWoodworkerProfile().getBrandName());
            shipment.setShipType("Giao hàng và lắp đặt bởi xưởng mộc");
            shipmentRepository.save(shipment);
        } else {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(request.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit("Giao hàng nhanh (GHN)");
            shipment.setShipType("Giao hàng bởi bên thứ 3 (GHN)");
            shipment.setToDistrictId(Integer.parseInt(request.getToDistrictId()));
            shipment.setToWardCode(request.getToWardCode());
            shipment.setFromDistrictId(Integer.parseInt(ww.getDistrictId()));
            shipment.setFromWardCode(ww.getWardCode());
            shipment.setFee(request.getPriceShipping());
            shipment.setGhnServiceId(request.getGhnServiceId());
            shipment.setGhnServiceTypeId(request.getGhnServiceTypeId());
            shipmentRepository.save(shipment);
        }

        contractServiceImpl.createOrderDeposit(serviceOrder);
    }

    @Override
    public ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting, String form, String desc) {
        ServiceOrder serviceOrder = orderRepository.findById(serviceOrderId).orElse(null);
        String currentStatus = serviceOrder.getStatus();
        String serviceName = serviceOrder.getAvailableService().getService().getServiceName();

        if (serviceOrder == null) {
            return null;
        }

        // Create new progress record with next status based on current status and role
        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setServiceOrder(serviceOrder);
        newOrderProgress.setCreatedTime(LocalDateTime.now());

        switch (serviceOrder.getRole()) {
            case "Woodworker":
                if (serviceName.equals(ServiceNameConstant.SALE)) {
                    serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
                }

                if (currentStatus == null || currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN) || currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    ConsultantAppointment consultantAppointment = new ConsultantAppointment();
                    if (serviceOrder.getConsultantAppointment() != null) {
                        consultantAppointment = serviceOrder.getConsultantAppointment();
                    }

                    consultantAppointment.setCreatedAt(LocalDateTime.now());
                    consultantAppointment.setDateTime(timeMeeting);
                    consultantAppointment.setMeetAddress(linkMeeting);
                    consultantAppointment.setContent(desc);
                    consultantAppointment.setForm(form);
                    consultantAppointmentRepository.save(consultantAppointment);

                    serviceOrder.setConsultantAppointment(consultantAppointment);
                    serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN);
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN);
                }

                break;
            case "Customer":
                if (currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    // Customer approving appointment
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_DUYET_LICH_HEN);
                    serviceOrder.setStatus(ServiceOrderStatusConstant.DA_DUYET_LICH_HEN);
                } else if (currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_HOP_DONG)) {
                    // Customer approving contract
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_DUYET_HOP_DONG);
                    serviceOrder.setStatus(ServiceOrderStatusConstant.DA_DUYET_HOP_DONG);
                } else if (currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_THIET_KE)) {
                    // Customer approving design
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_DUYET_THIET_KE);
                    serviceOrder.setStatus(ServiceOrderStatusConstant.DA_DUYET_THIET_KE);
                }
                break;
        }

        if (currentStatus.equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN) && serviceOrder.getRole().equals("Woodworker")) {
            // Empty
        } else {
            orderProgressRepository.save(newOrderProgress);
        }

        if (serviceOrder.getStatus().equals(ServiceOrderStatusConstant.DA_DUYET_THIET_KE)) {
            serviceOrder.setRole("Customer");
        } else {
            serviceOrder.setRole(serviceOrder.getRole().equals("Woodworker") ? "Customer" : "Woodworker");
        }
        serviceOrder.setFeedback(null);
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
        short qty = 0;
        AvailableService availableService =
                availableServiceRepository.findById(createServiceOrderPersonalizeRequest.getAvailableServiceId()).orElse(null);
        WoodworkerProfile ww = availableService.getWoodworkerProfile();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setAvailableService(availableService);
        serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        serviceOrder.setUser(user);
        serviceOrder.setDescription(createServiceOrderPersonalizeRequest.getNote());
        serviceOrder.setCreatedAt(LocalDateTime.now());
        serviceOrder.setRole("Woodworker");
        serviceOrder.setInstall(createServiceOrderPersonalizeRequest.getIsInstall());
        orderRepository.save(serviceOrder);

        List<RequestedProductPersonalizeDto> requestedProducts =
                createServiceOrderPersonalizeRequest.getRequestedProducts();

        for (RequestedProductPersonalizeDto requestedProductPersonalizeDto : requestedProducts) {
            RequestedProduct requestedProduct = new RequestedProduct();
            requestedProduct.setQuantity(requestedProductPersonalizeDto.getQuantity());
            requestedProduct.setServiceOrder(serviceOrder);
            requestedProduct.setCreatedAt(LocalDateTime.now());

            Category category = categoryRepository.findCategoriesByCategoryId(Long.parseLong(requestedProductPersonalizeDto.getCategoryId()));
            requestedProduct.setCategory(category);
            qty = (short) (qty + requestedProductPersonalizeDto.getQuantity());

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

        serviceOrder.setQuantity(qty);
        orderRepository.save(serviceOrder);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());
        orderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        orderProgressRepository.save(orderProgress);

        if (createServiceOrderPersonalizeRequest.getIsInstall()) {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(createServiceOrderPersonalizeRequest.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit(availableService.getWoodworkerProfile().getBrandName());
            shipment.setShipType("Giao hàng và lắp đặt bởi xưởng mộc");
            shipmentRepository.save(shipment);
        } else {
            Shipment shipment = new Shipment();
            shipment.setServiceOrder(serviceOrder);
            shipment.setToAddress(createServiceOrderPersonalizeRequest.getAddress());
            shipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            shipment.setShippingUnit("Giao hàng nhanh (GHN)");
            shipment.setShipType("Giao hàng bởi bên thứ 3 (GHN)");
            shipment.setToDistrictId(Integer.parseInt(createServiceOrderPersonalizeRequest.getToDistrictId()));
            shipment.setToWardCode(createServiceOrderPersonalizeRequest.getToWardCode());
            shipment.setFromDistrictId(Integer.parseInt(ww.getDistrictId()));
            shipment.setFromWardCode(ww.getWardCode());
            shipment.setFee(createServiceOrderPersonalizeRequest.getPriceShipping());
            shipment.setGhnServiceId(createServiceOrderPersonalizeRequest.getGhnServiceId());
            shipment.setGhnServiceTypeId(createServiceOrderPersonalizeRequest.getGhnServiceTypeId());
            shipmentRepository.save(shipment);
        }

        return serviceOrder;
    }

    @Override
    public void addProductImage(List<ProductImagesDto> productImagesDtos, Long serviceId)
    {
        ServiceOrder serviceOrder = orderRepository.findById(serviceId).orElse(null);

        if (serviceOrder.getStatus().equals(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_THIET_KE)) {
            serviceOrder.setRole("Customer");
            serviceOrder.setFeedback("");
            orderRepository.save(serviceOrder);

            List<ProductImages> productImages = new ArrayList<>();
            for (ProductImagesDto productImagesDto : productImagesDtos)
            {
                RequestedProduct requestedProduct =
                        requestedProductRepository.findRequestedProductByRequestedProductId(productImagesDto.getProductId());
                ProductImages productImage = productImagesRepository.findByRequestedProduct(requestedProduct).stream().findFirst().orElse(null);
                productImage.setMediaUrls(productImagesDto.getMediaUrls());
                productImage.setType("design");
                productImagesRepository.save(productImage);

                productImages.add(productImage);
            }
        } else {
            OrderProgress orderProgress = new OrderProgress();
            orderProgress.setServiceOrder(serviceOrder);
            orderProgress.setCreatedTime(LocalDateTime.now());
            orderProgress.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_THIET_KE);
            orderProgressRepository.save(orderProgress);

            serviceOrder.setRole("Customer");
            serviceOrder.setFeedback("");
            serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_CHO_KHACH_DUYET_THIET_KE);
            orderRepository.save(serviceOrder);

            List<ProductImages> productImages = new ArrayList<>();
            for (ProductImagesDto productImagesDto : productImagesDtos)
            {
                RequestedProduct requestedProduct =
                        requestedProductRepository.findRequestedProductByRequestedProductId(productImagesDto.getProductId());
                ProductImages productImage = new ProductImages();
                productImage.setRequestedProduct(requestedProduct);
                productImage.setMediaUrls(productImagesDto.getMediaUrls());
                productImage.setType("design");
                productImagesRepository.save(productImage);

                productImages.add(productImage);
            }
        }
    }

    @Override
    public void addProductFinishImage(List<ProductImagesDto> productImagesDtos, Long serviceId) {
        ServiceOrder serviceOrder = orderRepository.findById(serviceId).orElse(null);
        List<ProductImages> productImages = new ArrayList<>();
        for (ProductImagesDto productImagesDto : productImagesDtos)
        {
            RequestedProduct requestedProduct =
                    requestedProductRepository.findRequestedProductByRequestedProductId(productImagesDto.getProductId());
            ProductImages productImage = new ProductImages();
            productImage.setRequestedProduct(requestedProduct);
            productImage.setMediaUrls(productImagesDto.getMediaUrls());
            productImage.setType("finish");
            productImagesRepository.save(productImage);

            productImages.add(productImage);
        }

        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setServiceOrder(serviceOrder);
        orderProgress.setCreatedTime(LocalDateTime.now());
        orderProgress.setStatus(ServiceOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
        orderProgressRepository.save(orderProgress);

        serviceOrder.setRole("Customer");
        serviceOrder.setFeedback("");
        serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
        orderRepository.save(serviceOrder);
    }
}