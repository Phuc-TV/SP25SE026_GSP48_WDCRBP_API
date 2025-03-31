package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    // 1. Lấy danh sách đơn dịch vụ theo userId hoặc woodworkerId và role
    @GetMapping("/listOrder")
    public CoreApiResponse listOrdersByUserOrWoodworker(
            @RequestParam Long id,
            @RequestParam String role) {

        List<ServiceOrder> serviceOrders = serviceOrderService.listServiceOrderByUserIdOrWwId(id, role);

        return CoreApiResponse.success(serviceOrders);
    }

    // 2. Tạo đơn dịch vụ tuỳ chỉnh
    @PostMapping("/createCustomizeOrder")
    public CoreApiResponse createCustomizeOrder(@RequestBody CreateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderService.addServiceOrderCustomize(request);
        return CoreApiResponse.success(serviceOrder);
    }

    // 3. Thợ mộc hoặc khách hàng chấp nhận đơn và thiết lập lịch hẹn
    @PostMapping("/accept")
    public CoreApiResponse acceptServiceOrder(
            @RequestParam Long serviceOrderId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeMeeting,
            @RequestParam String linkMeeting) {
        ServiceOrder serviceOrder = serviceOrderService.acceptServiceOrder(serviceOrderId, timeMeeting, linkMeeting);

        return CoreApiResponse.success(serviceOrder);
    }

    // 4. Khách hàng gửi phản hồi
    @PostMapping("/feedback")
    public CoreApiResponse customerFeedback(
            @RequestParam Long serviceOrderId,
            @RequestParam String feedback) {
        ServiceOrder serviceOrder = serviceOrderService.customerFeedback(serviceOrderId, feedback);

        return CoreApiResponse.success(serviceOrder);
    }

    @PostMapping("/createPersonalOrder")
    public CoreApiResponse createPersonalOrder(@RequestBody CreateServiceOrderPersonalizeRequest request)
    {
        ServiceOrder serviceOrder = serviceOrderService.createServiceOrderPersonalize(request);

        return CoreApiResponse.success(serviceOrder);
    }
}