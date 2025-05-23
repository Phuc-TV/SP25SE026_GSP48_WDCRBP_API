package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.LoginOtpRes;
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
    public CoreApiResponse<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginDto){
        try {
            AuthenticationResponse token = authService.login(loginDto);
            return  CoreApiResponse.success(token, "Đăng nhập thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Login failed: " + e.getMessage());
        }
    }

    @PostMapping(value = { "/register"})
    public CoreApiResponse<String> signup(@Valid @RequestBody SignupRequest signupDto){
        try{
            String response = authService.signup(signupDto);
            return CoreApiResponse.success(response, "Đăng ký thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Register failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/send-otp")
    public CoreApiResponse<?> sendOtp(@RequestParam String email) {
        try {
            authService.sendOtpToEmail(email);
            return CoreApiResponse.success("OTP đã gửi đến email của bạn");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login-otp")
    public CoreApiResponse<LoginOtpRes> loginWithOtp(@Valid @RequestBody LoginOtpRequest request) {
        try {
            LoginOtpRes response = authService.loginWithOtp(request);
            return CoreApiResponse.success(response, "Đăng nhập thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/verification-otp")
    public CoreApiResponse<?> VerificationAccountWithOtp(@Valid @RequestBody LoginOtpRequest request) {
        try {
            authService.otpChangeStatusAccount(request);
            return CoreApiResponse.success("Bạn đã xác thực tài khoản thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "xác thực không thành công: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public CoreApiResponse<Void> resetPassword(@RequestParam String email, @RequestParam String otp,
                                               @RequestBody @Valid ResetPasswordOTPRequest request) {
        try {
            authService.resetPasswordWithOTP(email, otp, request);
            return CoreApiResponse.success("Đặt lại mật khẩu thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Đặt lại mật khẩu thất bại: " + e.getMessage());
        }
    }
}
