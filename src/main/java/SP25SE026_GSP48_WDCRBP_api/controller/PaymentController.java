package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentWalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PaymentRest;
import SP25SE026_GSP48_WDCRBP_api.service.VNPayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/payment")
public class PaymentController {

    private final VNPayService vnPayService;

    public PaymentController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> pay(@Valid @RequestBody PaymentOrderRequest request) {
        try {
            PaymentRest paymentResponse = vnPayService.processOrderPayment(request);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment: " + e.getMessage());
        }
    }

    @PostMapping("/pay-service-pack")
    public ResponseEntity<?> payServicePack(@Valid @RequestBody PaymentServicePackRequest request) {
        try {
            PaymentRest paymentResponse = vnPayService.processServicePackPayment(request);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing service pack payment: " + e.getMessage());
        }
    }

    @PostMapping("/top-up-wallet")
    public ResponseEntity<?> payWallet(@Valid @RequestBody PaymentWalletRequest request) {
        try {
            PaymentRest paymentResponse = vnPayService.processWalletPayment(request);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing wallet top-up: " + e.getMessage());
        }
    }

}
