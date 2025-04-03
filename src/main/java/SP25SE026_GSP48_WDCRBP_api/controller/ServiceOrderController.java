package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ProductImagesDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceDepositDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ProductImages;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderDetailRes;
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

    // 3. Thợ mộc hoặc khách hàng chấp nhận đơn và thiết lập lịch hẹn
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/accept")
    public CoreApiResponse acceptServiceOrder(
            @RequestParam Long serviceOrderId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeMeeting,
            @RequestParam String linkMeeting,
            @RequestParam String form,
            @RequestParam String desc
    ) {
        ServiceOrder serviceOrder = serviceOrderService.acceptServiceOrder(serviceOrderId, timeMeeting, linkMeeting, form, desc);

        return CoreApiResponse.success("Success");
    }

    // 4. Khách hàng gửi phản hồi
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/feedback")
    public CoreApiResponse customerFeedback(
            @RequestParam Long serviceOrderId,
            @RequestParam String feedback) {
        ServiceOrder serviceOrder = serviceOrderService.customerFeedback(serviceOrderId, feedback);

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
    public CoreApiResponse addProductImage(@RequestBody List<ProductImagesDto> request)
    {
        List<ProductImages> productImages = serviceOrderService.addProductImage(request);

        List<ProductImagesDto> productImagesDtos = productImages.stream()
                .map(productImage -> modelMapper.map(productImage, ProductImagesDto.class)).toList();
        return CoreApiResponse.success(productImagesDtos);
    }
}