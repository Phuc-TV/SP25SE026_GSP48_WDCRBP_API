package SP25SE026_GSP48_WDCRBP_API.service;

import SP25SE026_GSP48_WDCRBP_API.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_API.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_API.model.responseModel.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse login(LoginRequest loginDto);
    String signup(SignupRequest signupDto);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
