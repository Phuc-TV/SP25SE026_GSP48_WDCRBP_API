package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ProductImagesDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceDepositDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ProductImages;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderListItemRes;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @Autowired
    private ModelMapper modelMapper;

    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping
    public CoreApiResponse<List<ServiceOrderListItemRes>> getAllOrders() {
        List<ServiceOrderListItemRes> serviceOrders = serviceOrderService.getAll();

        return CoreApiResponse.success(serviceOrders);
    }

    // 1. Lấy danh sách đơn dịch vụ theo userId hoặc woodworkerId và role
    @GetMapping("/listOrder")
    public CoreApiResponse listOrdersByUserOrWoodworker(
            @RequestParam Long id,
            @RequestParam String role) {

        List<ServiceOrderDto> serviceOrders = serviceOrderService.listServiceOrderByUserIdOrWwId(id, role);

        return CoreApiResponse.success(serviceOrders);
    }

    @GetMapping("/{id}")
    public CoreApiResponse getServiceOrderById(
            @PathVariable Long id) {
        try {
            ServiceOrderDetailRes serviceOrder = serviceOrderService.getServiceDetailById(id);
            return CoreApiResponse.success(serviceOrder);
        } catch (Exception e) {
            return CoreApiResponse.error(e.getMessage());
        }
    }

    // 2. Tạo đơn dịch vụ tuỳ chỉnh
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/createCustomizeOrder")
    public CoreApiResponse createCustomizeOrder(@RequestBody CreateServiceOrderCusRequest request) {
        return serviceOrderService.addServiceOrderCustomize(request);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/createSaleOrder")
    public CoreApiResponse createSaleOrder(@RequestBody CreateServiceOrderSaleRequest request) {
        serviceOrderService.addSaleOrder(request);

        return CoreApiResponse.success("Success");
    }

    // 3. xưởng mộc hoặc khách hàng chấp nhận đơn và thiết lập lịch hẹn
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/accept")
    public CoreApiResponse acceptServiceOrder(
            @RequestBody WwAppointmentCreateRequest request) {
        try {
            ServiceOrder serviceOrder = serviceOrderService.acceptServiceOrder(request.getServiceOrderId(), request.getTimeMeeting(), request.getMeetAddress(), request.getForm(), request.getDesc());

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    // 4. Khách hàng gửi phản hồi
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/feedback")
    public CoreApiResponse customerFeedback(
            @RequestBody CusFeedbackCreateRequest request) {
        ServiceOrder serviceOrder = serviceOrderService.customerFeedback(request.getServiceOrderId(), request.getFeedback());

        return CoreApiResponse.success("Success");
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/createPersonalOrder")
    public CoreApiResponse createPersonalOrder(@RequestBody CreateServiceOrderPersonalizeRequest request)
    {
        ServiceOrder serviceOrder = serviceOrderService.createServiceOrderPersonalize(request);

        return CoreApiResponse.success("Success");
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/addProductImage")
    public CoreApiResponse addProductImage(@RequestBody List<ProductImagesDto> request,
                                           @RequestParam Long serviceId)
    {
        serviceOrderService.addProductImage(request, serviceId);

        return CoreApiResponse.success("Success");
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/finish-product-image")
    public CoreApiResponse addFinishProductImage(@RequestBody List<ProductImagesDto> request, @RequestParam Long serviceId)
    {
        serviceOrderService.addProductFinishImage(request,serviceId);

        return CoreApiResponse.success("Success");
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/cancel")
    public CoreApiResponse cancel(@RequestBody ServiceOrderIdRequest request)
    {
        serviceOrderService.cancelOrder(request.getServiceOrderId());

        return CoreApiResponse.success("Success");
    }
}