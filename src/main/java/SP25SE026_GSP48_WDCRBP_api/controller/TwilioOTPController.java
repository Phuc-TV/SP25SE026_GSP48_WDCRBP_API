package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.service.TwilioOTPService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/otp")
public class TwilioOTPController {
    @Autowired
    private TwilioOTPService twilioOTPService;

    // Endpoint to start a new verification
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/start-verification")
    public ResponseEntity<?> startVerification(@RequestParam String toPhoneNumber) {
        var verification = twilioOTPService.startVerification(toPhoneNumber);
        return ResponseEntity.ok(verification);
    }

    // Endpoint to check verification
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/check-verification")
    public ResponseEntity<?> checkVerification(@RequestParam String toPhoneNumber, @RequestParam String code) {
        var verificationCheck = twilioOTPService.checkVerification(toPhoneNumber, code);
        return ResponseEntity.ok(verificationCheck);
    }

    // Endpoint to fetch a specific verification
    @GetMapping("/fetch-verification/{sid}")
    public ResponseEntity<?> fetchVerification(@PathVariable String sid) {
        var verification = twilioOTPService.fetchVerification(sid);
        return ResponseEntity.ok(verification);
    }
}
