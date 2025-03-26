package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRest;
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
    public ResponseEntity<?> pay(@Valid @RequestBody PaymentRequest request) {
        try {
            PaymentRest paymentResponse = vnPayService.processPayment(request);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment: " + e.getMessage());
        }
    }
}
