package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ResetPasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ResetPasswordRest;
import SP25SE026_GSP48_WDCRBP_api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginDto){
        AuthenticationResponse token = authService.login(loginDto);
        return  ResponseEntity.ok(token);
    }

    @PostMapping(value = { "/register"})
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupDto){
        String response = authService.signup(signupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ResetPasswordRest> sendOtp(@RequestParam String email) {
        try {
            // Step 1: Check if the email exists and send OTP
            ResetPasswordRest response = authService.sendOtpToEmail(email);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResetPasswordRest("Error", e.getMessage(), null));
        }
    }

    // Endpoint to verify OTP and reset the password
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordRest> verifyOtpAndResetPassword(
            @RequestParam String otp,
            @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        try {
            // Step 2: Verify OTP and reset password
            ResetPasswordRest response = authService.verifyOtpAndResetPassword(otp, resetPasswordRequest.getEmail(),
                    resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResetPasswordRest("Error", e.getMessage(), null));
        }
    }
}
