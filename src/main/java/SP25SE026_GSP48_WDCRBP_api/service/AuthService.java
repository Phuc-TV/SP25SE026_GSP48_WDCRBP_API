package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.LoginOtpRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ResetPasswordOTPRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.LoginOtpRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse login(LoginRequest loginDto);
    String signup(SignupRequest signupDto);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void sendOtpToEmail(String email);
    LoginOtpRes loginWithOtp(LoginOtpRequest request);
    void otpChangeStatusAccount(LoginOtpRequest request);
    void resetPasswordWithOTP(String email, String otp, ResetPasswordOTPRequest request);
}
