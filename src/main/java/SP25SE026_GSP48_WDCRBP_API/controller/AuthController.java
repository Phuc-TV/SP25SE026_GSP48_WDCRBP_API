package SP25SE026_GSP48_WDCRBP_API.controller;

import SP25SE026_GSP48_WDCRBP_API.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_API.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_API.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_API.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginDto){
        AuthenticationResponse token = authService.login(loginDto);
        return  ResponseEntity.ok(token);
    }

    @PostMapping(value = {"/signup", "/register"})
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
}
