package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.GuaranteeOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateGuaranteeOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.GuaranteeOrderService;
import SP25SE026_GSP48_WDCRBP_api.service.QuotationDetailService;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GuaranteeOrderServiceImpl implements GuaranteeOrderService  {
    private final UserRepository userRepository;
    private final AvailableServiceRepository availableServiceRepository;
    private final GuaranteeOrderRepository guaranteeOrderRepository;
    private final RequestedProductRepository requestedProductRepository;
    private final OrderProgressRepository orderProgressRepository;
    private final ShipmentRepository shipmentRepository;
    private final ModelMapper modelMapper;
    private final WoodworkerProfileRepository woodworkerProfileRepository;
    private final ServiceOrderService serviceOrderService;
    private final ConsultantAppointmentRepository consultantAppointmentRepository;
    private final QuotationDetailService quotationDetailService;

    public GuaranteeOrderServiceImpl(UserRepository userRepository, AvailableServiceRepository availableServiceRepository, GuaranteeOrderRepository guaranteeOrderRepository, RequestedProductRepository requestedProductRepository, OrderProgressRepository orderProgressRepository, ShipmentRepository shipmentRepository, ModelMapper modelMapper, WoodworkerProfileRepository woodworkerProfileRepository, ServiceOrderService serviceOrderService, ConsultantAppointmentRepository consultantAppointmentRepository, QuotationDetailService quotationDetailService) {
        this.userRepository = userRepository;
        this.availableServiceRepository = availableServiceRepository;
        this.guaranteeOrderRepository = guaranteeOrderRepository;
        this.requestedProductRepository = requestedProductRepository;
        this.orderProgressRepository = orderProgressRepository;
        this.shipmentRepository = shipmentRepository;
        this.modelMapper = modelMapper;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
        this.serviceOrderService = serviceOrderService;
        this.consultantAppointmentRepository = consultantAppointmentRepository;
        this.quotationDetailService = quotationDetailService;
    }

    @Override
    public GuaranteeOrderListItemRes createGuaranteeOrder(CreateGuaranteeOrderRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        AvailableService availableService =
                availableServiceRepository.findById(request.getAvailableServiceId()).orElse(null);
        WoodworkerProfile ww = availableService.getWoodworkerProfile();
        AvailableService guaranteeOrderService =
                availableServiceRepository.findAvailableServicesByWoodworkerProfile(ww).stream().filter(service -> service.getService().getServiceName().equals(ServiceNameConstant.GUARANTEE)).findFirst().orElse(null);
        RequestedProduct requestedProduct = requestedProductRepository.findRequestedProductByRequestedProductId(request.getRequestedProductId());

        GuaranteeOrder order = new GuaranteeOrder();
        order.setAvailableService(guaranteeOrderService);
        order.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        order.setUser(user);
        order.setDescription(request.getNote());
        order.setCreatedAt(LocalDateTime.now());
        order.setRole("Woodworker");
        order.setInstall(request.getIsInstall());
        order.setProductCurrentStatus(request.getProductCurrentStatus());
        order.setCurrentProductImgUrls(request.getCurrentProductImgUrls());
        order.setRequestedProduct(requestedProduct);
        order.setShipFee((float)request.getPriceShipping());
        order.setGuaranteeError(request.getGuaranteeError());
        order.setIsGuarantee(request.getIsGuarantee());
        guaranteeOrderRepository.save(order);

        //Create OrderProgress
        OrderProgress orderProgress = new OrderProgress();
        orderProgress.setGuaranteeOrder(order);
        orderProgress.setCreatedTime(LocalDateTime.now());
        orderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN);
        orderProgressRepository.save(orderProgress);

        if (request.getIsInstall()) {
            Shipment getShipment = new Shipment();
            getShipment.setGuaranteeOrder(order);
            getShipment.setToAddress(ww.getAddress());
            getShipment.setToDistrictId(Integer.parseInt(ww.getDistrictId()));
            getShipment.setToWardCode(ww.getWardCode());
            getShipment.setFromAddress(request.getAddress());
            getShipment.setFromDistrictId(Integer.parseInt(request.getToDistrictId()));
            getShipment.setFromWardCode(request.getToWardCode());
            getShipment.setShippingUnit("Giao hàng nhanh (GHN)");
            getShipment.setShipType("Lấy hàng bởi bên thứ 3 (GHN)");
            getShipment.setFee(request.getPriceShipping());
            getShipment.setGhnServiceId(request.getGhnServiceId());
            getShipment.setGhnServiceTypeId(request.getGhnServiceTypeId());
            shipmentRepository.save(getShipment);

            Shipment deliverShipment = new Shipment();
            deliverShipment.setGuaranteeOrder(order);
            deliverShipment.setToAddress(request.getAddress());
            deliverShipment.setFromAddress(availableService.getWoodworkerProfile().getAddress());
            deliverShipment.setShippingUnit(availableService.getWoodworkerProfile().getBrandName());
            deliverShipment.setShipType("Giao hàng và lắp đặt bởi xưởng mộc");
            shipmentRepository.save(deliverShipment);
        } else {
            Shipment getShipment = new Shipment();
            getShipment.setGuaranteeOrder(order);
            getShipment.setToAddress(ww.getAddress());
            getShipment.setToDistrictId(Integer.parseInt(ww.getDistrictId()));
            getShipment.setToWardCode(ww.getWardCode());
            getShipment.setFromAddress(request.getAddress());
            getShipment.setFromDistrictId(Integer.parseInt(request.getToDistrictId()));
            getShipment.setFromWardCode(request.getToWardCode());
            getShipment.setShippingUnit("Giao hàng nhanh (GHN)");
            getShipment.setShipType("Lấy hàng bởi bên thứ 3 (GHN)");
            getShipment.setFee(request.getPriceShipping());
            getShipment.setGhnServiceId(request.getGhnServiceId());
            getShipment.setGhnServiceTypeId(request.getGhnServiceTypeId());
            shipmentRepository.save(getShipment);

            Shipment deliverShipment = new Shipment();
            deliverShipment.setGuaranteeOrder(order);
            deliverShipment.setToAddress(request.getAddress());
            deliverShipment.setToDistrictId(Integer.parseInt(request.getToDistrictId()));
            deliverShipment.setToWardCode(request.getToWardCode());
            deliverShipment.setFromAddress(ww.getAddress());
            deliverShipment.setFromDistrictId(Integer.parseInt(ww.getDistrictId()));
            deliverShipment.setFromWardCode(ww.getWardCode());
            deliverShipment.setShippingUnit("Giao hàng nhanh (GHN)");
            deliverShipment.setShipType("Giao hàng bởi bên thứ 3 (GHN)");
            deliverShipment.setFee(request.getPriceShipping());
            deliverShipment.setGhnServiceId(request.getGhnServiceId());
            deliverShipment.setGhnServiceTypeId(request.getGhnServiceTypeId());
            shipmentRepository.save(deliverShipment);
        }

        return modelMapper.map(order, GuaranteeOrderListItemRes.class);
    }

    @Override
    public List<GuaranteeOrderListItemRes> listGuaranteeOrderByUserIdOrWwId(Long id, String role) {
        List<GuaranteeOrder> orders = new ArrayList<>();

        if (role.equals("Customer")) {
            User user = userRepository.findUserByUserId(id);
            orders = guaranteeOrderRepository.findByUser(user);
        } else {
            if (role.equals("Woodworker")) {
                WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(id);
                List<AvailableService> availableServices =
                        availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworkerProfile);
                for (AvailableService availableService : availableServices) {
                    List<GuaranteeOrder> serviceOrders = guaranteeOrderRepository.findByAvailableService(availableService);
                    orders.addAll(serviceOrders);
                }
            } else {
                return null;
            }
        }

        List<GuaranteeOrderListItemRes> guaranteeOrderListItemRes = new ArrayList<>();
        for (GuaranteeOrder order : orders) {
            GuaranteeOrderListItemRes guaranteeOrder = modelMapper.map(order, GuaranteeOrderListItemRes.class);
            guaranteeOrder.setWoodworkerUser(modelMapper.map(order.getAvailableService().getWoodworkerProfile().getUser(), UserDetailRes.class));
            guaranteeOrder.setWoodworker(modelMapper.map(order.getAvailableService().getWoodworkerProfile(), WoodworkerProfileListItemRes.class));
            guaranteeOrder.setUser(modelMapper.map(order.getUser(), UserDetailRes.class));
            guaranteeOrderListItemRes.add(guaranteeOrder);
        }

        return guaranteeOrderListItemRes;
    }

    @Override
    public GuaranteeOrderDetailRes getGuaranteeDetailById(Long id) {
        GuaranteeOrder order = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(id);
        ServiceOrderDetailRes serviceOrderDetailRes = serviceOrderService.getServiceDetailById(order.getRequestedProduct().getServiceOrder().getOrderId());

        GuaranteeOrderDetailRes guaranteeOrderDetailRes = modelMapper.map(order, GuaranteeOrderDetailRes.class);
        guaranteeOrderDetailRes.setWoodworkerUser(modelMapper.map(order.getAvailableService().getWoodworkerProfile().getUser(), UserDetailRes.class));
        guaranteeOrderDetailRes.setWoodworker(modelMapper.map(order.getAvailableService().getWoodworkerProfile(), WoodworkerProfileListItemRes.class));
        guaranteeOrderDetailRes.setUser(modelMapper.map(order.getUser(), UserDetailRes.class));
        guaranteeOrderDetailRes.setServiceOrderDetail(serviceOrderDetailRes);

        return guaranteeOrderDetailRes;
    }

    @Override
    public void acceptGuaranteeOrder(Long guaranteeOrderId, LocalDateTime timeMeeting, String linkMeeting, String form, String desc) {
        GuaranteeOrder order = guaranteeOrderRepository.findById(guaranteeOrderId).orElse(null);
        String currentStatus = order.getStatus();

        // Create new progress record with next status based on current status and role
        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setGuaranteeOrder(order);
        newOrderProgress.setCreatedTime(LocalDateTime.now());

        switch (order.getRole()) {
            case "Woodworker":
                if (currentStatus == null || currentStatus.equals(GuaranteeOrderStatusConstant.DANG_CHO_THO_MOC_XAC_NHAN) || currentStatus.equals(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    // Woodworker accepting initial order, setting appointment
                    ConsultantAppointment consultantAppointment = new ConsultantAppointment();
                    if (order.getConsultantAppointment() != null) {
                        consultantAppointment = order.getConsultantAppointment();
                    }

                    consultantAppointment.setCreatedAt(LocalDateTime.now());
                    consultantAppointment.setDateTime(timeMeeting);
                    consultantAppointment.setMeetAddress(linkMeeting);
                    consultantAppointment.setContent(desc);
                    consultantAppointment.setForm(form);
                    consultantAppointmentRepository.save(consultantAppointment);

                    order.setConsultantAppointment(consultantAppointment);
                    order.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN);
                    order.setIsGuarantee(false);
                    newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN);
                }

                break;
            case "Customer":
                if (currentStatus.equals(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN)) {
                    // Customer approving appointment
                    newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_LICH_HEN);
                    order.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_LICH_HEN);
                } else if (currentStatus.equals(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_BAO_GIA)) {
                    // Customer approving quotation
                    newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA);
                    order.setStatus(GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA);
                }
                break;
        }

        if (currentStatus.equals(GuaranteeOrderStatusConstant.DANG_CHO_KHACH_DUYET_LICH_HEN) && order.getRole().equals("Woodworker")) {
            // Empty
        } else {
            orderProgressRepository.save(newOrderProgress);
        }

        if (order.getStatus().equals(GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA)) {
            order.setRole("Customer");
        } else {
            order.setRole(order.getRole().equals("Woodworker") ? "Customer" : "Woodworker");
        }
        order.setFeedback(null);
        guaranteeOrderRepository.save(order);
    }

    @Override
    public void confirmReceiveProduct(Long guaranteeOrderId) {
        GuaranteeOrder order = guaranteeOrderRepository.findById(guaranteeOrderId).orElse(null);
        order.setStatus(GuaranteeOrderStatusConstant.DANG_SUA_CHUA);
        order.setRole("Woodworker");
        order.setFeedback("");
        guaranteeOrderRepository.save(order);

        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setGuaranteeOrder(order);
        newOrderProgress.setCreatedTime(LocalDateTime.now());
        newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_SUA_CHUA);
        orderProgressRepository.save(newOrderProgress);
    }

    @Override
    public void confirmFinishRepair(Long guaranteeOrderId) {
        GuaranteeOrder order = guaranteeOrderRepository.findById(guaranteeOrderId).orElse(null);
        order.setStatus(GuaranteeOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
        order.setRole("Customer");
        order.setFeedback("");
        guaranteeOrderRepository.save(order);

        OrderProgress newOrderProgress = new OrderProgress();
        newOrderProgress.setGuaranteeOrder(order);
        newOrderProgress.setCreatedTime(LocalDateTime.now());
        newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT);
        orderProgressRepository.save(newOrderProgress);
    }

    @Override
    public void customerFeedback(Long guaranteeOrderId, String feedback) {
        GuaranteeOrder order = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId((guaranteeOrderId));

        if (order.getRole().equals("Customer")) {
            order.setFeedback(feedback);
            order.setRole("Woodworker");
            guaranteeOrderRepository.save(order);
        }
    }

    @Override
    public void acceptGuaranteeFreeOrder(Long guaranteeOrderId) {
        quotationDetailService.saveQuotationForFreeGuarantee(guaranteeOrderId);
    }
}
