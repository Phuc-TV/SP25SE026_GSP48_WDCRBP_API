package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.GuaranteeOrderDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.GuaranteeOrderListItemRes;
import SP25SE026_GSP48_WDCRBP_api.service.GuaranteeOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/guarantee-orders")
public class GuaranteeOrderController {
    @Autowired
    private GuaranteeOrderService guaranteeOrderService;

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping
    public CoreApiResponse createOrder(@RequestBody CreateGuaranteeOrderRequest request)
    {
        try {
            guaranteeOrderService.createGuaranteeOrder(request);

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @GetMapping
    public CoreApiResponse listOrdersByUserOrWoodworker(
            @RequestParam Long id,
            @RequestParam String role) {
        try {
            List<GuaranteeOrderListItemRes> orders = guaranteeOrderService.listGuaranteeOrderByUserIdOrWwId(id, role);

            return CoreApiResponse.success(orders);
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @GetMapping("/{id}")
    public CoreApiResponse getOrderById(
            @PathVariable Long id) {
        try {
            GuaranteeOrderDetailRes guaranteeOrder = guaranteeOrderService.getGuaranteeDetailById(id);
            return CoreApiResponse.success(guaranteeOrder);
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/accept")
    public CoreApiResponse acceptOrder(
            @RequestBody WwAppointmentCreateRequest request) {
        try {
            guaranteeOrderService.acceptGuaranteeOrder(request.getServiceOrderId(), request.getTimeMeeting(), request.getMeetAddress(), request.getForm(), request.getDesc());

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/receive-confirmation")
    public CoreApiResponse receiveProductConfirmation(
            @RequestBody GuaranteeOrderIdRequest request) {
        try {
            guaranteeOrderService.confirmReceiveProduct(request.getGuaranteeOrderId());

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/finish-confirmation")
    public CoreApiResponse finishRepairConfirmation(
            @RequestBody GuaranteeOrderIdRequest request) {
        try {
            guaranteeOrderService.confirmFinishRepair(request.getGuaranteeOrderId());

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/feedback")
    public CoreApiResponse customerFeedback(
            @RequestBody CusFeedbackCreateRequest request) {
        try {
            guaranteeOrderService.customerFeedback(request.getServiceOrderId(), request.getFeedback());

            return CoreApiResponse.success("Success");
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi hệ thống");
        }
    }
}