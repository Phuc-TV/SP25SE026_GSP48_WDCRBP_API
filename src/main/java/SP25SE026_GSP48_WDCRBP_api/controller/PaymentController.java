package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentWalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PaymentRes;
import SP25SE026_GSP48_WDCRBP_api.service.VNPayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final VNPayService vnPayService;

    public PaymentController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @PostMapping("/create-payment")
    public CoreApiResponse<PaymentRes> pay(@Valid @RequestBody PaymentOrderRequest request) {
        try {
            PaymentRes paymentResponse = vnPayService.processOrderPayment(request);
            return CoreApiResponse.success(paymentResponse, "Tạo liên kết thanh toán thành công.");
        } catch (WDCRBPApiException e) {
            return CoreApiResponse.error(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Lỗi không xác định khi xử lý thanh toán đơn hàng." + e.getMessage());
        }
    }

    @PostMapping("/pay-service-pack")
    public CoreApiResponse<PaymentRes> payServicePack(@Valid @RequestBody PaymentServicePackRequest request) {
        try {
            PaymentRes paymentResponse = vnPayService.processServicePackPayment(request);
            return CoreApiResponse.success(paymentResponse, "Tạo liên kết thanh toán gói dịch vụ thành công.");
        } catch (WDCRBPApiException e) {
            return CoreApiResponse.error(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Lỗi không xác định khi xử lý thanh toán gói dịch vụ."+ e.getMessage());
        }
    }

    @PostMapping("/top-up-wallet")
    public CoreApiResponse<PaymentRes> payWallet(@Valid @RequestBody PaymentWalletRequest request) {
        try {
            PaymentRes paymentResponse = vnPayService.processWalletPayment(request);
            return CoreApiResponse.success(paymentResponse, "Tạo liên kết nạp ví thành công.");
        } catch (WDCRBPApiException e) {
            return CoreApiResponse.error(e.getStatus(), e.getMessage(), null);
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,"Lỗi không xác định khi xử lý nạp tiền vào ví."+ e.getMessage());
        }
    }
}
