package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AccessToken;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RefreshToken;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ResetPasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ResetPasswordRest;
import SP25SE026_GSP48_WDCRBP_api.repository.AccessTokenRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.RefreshTokenRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.security.JwtTokenProvider;
import SP25SE026_GSP48_WDCRBP_api.service.AuthService;
import SP25SE026_GSP48_WDCRBP_api.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                       AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    @Autowired
    private MailService mailService;

    private static final Map<String, String> otpMap = new HashMap<>();

    @Override
    public AuthenticationResponse login(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmailOrPhone(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findUserByEmailOrPhone(loginDto.getEmailOrPhone(), loginDto.getEmailOrPhone())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.BAD_REQUEST, "User not found"));

        // Truyền thẳng User vào để JWT chứa thông tin User
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        revokeRefreshToken(accessToken);
        RefreshToken savedRefreshToken = saveUserRefreshToken(refreshToken);

        revokeAllUserAccessTokens(user);
        saveUserAccessToken(user, accessToken, savedRefreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void revokeRefreshToken(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken);
        if (token != null) {
            RefreshToken refreshToken = token.getRefreshToken();
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    private void revokeAllUserAccessTokens(User user) {
        var validUserTokens = accessTokenRepository.findAllValidTokensByUser(user.getUserId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(accessToken -> {
            accessToken.setRevoked(true);
            accessToken.setExpired(true);
        });
        accessTokenRepository.saveAll(validUserTokens);
    }

    private void saveUserAccessToken(User user, String jwtToken, RefreshToken refreshToken) {
        var token = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .revoked(false)
                .expired(false)
                .build();
        accessTokenRepository.save(token);
    }

    private RefreshToken saveUserRefreshToken(String jwtToken) {
        var token = RefreshToken.builder()
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .build();
        return refreshTokenRepository.save(token);
    }

    @Override
    public String signup(SignupRequest signupDto) {
        // add check if email already exists
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        user.setRole("Customer");

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Invalid token format");
        }

        String refreshToken = authHeader.substring(7);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Refresh token not found"));

        User user = jwtTokenProvider.getUserFromToken(refreshToken); // 🔥 Load User trực tiếp từ JWT

        if (user != null && jwtTokenProvider.isTokenValid(refreshToken, user.getEmail())
                && !token.isRevoked() && !token.isExpired()
        ) {
            // map user to authentication
            Authentication userAuthentication = new UsernamePasswordAuthenticationToken(user, null, null);
            var accessToken = jwtTokenProvider.generateAccessToken(user);

            revokeAllUserAccessTokens(user);
            saveUserAccessToken(user, accessToken, token);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
    }

    @Override
    public ResetPasswordRest sendOtpToEmail(String email) {
        // Check if user exists with the provided email
        Optional<User> userOptional = userRepository.findUserByEmailOrPhone(email, email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with the provided email not found.");
        }

        User user = userOptional.get();

        // Generate OTP
        String otp = generateOtp();

        // Store OTP temporarily (for this example, using an in-memory map)
        otpMap.put(user.getEmail(), otp);

        // Send OTP to email
        sendOtpEmail(user.getEmail(), otp);

        // Return response
        ResetPasswordRest response = new ResetPasswordRest();
        response.setStatus("Success");
        response.setMessage("OTP sent to your email.");
        return response;
    }

    // Generate a random OTP (6 digits)
    private String generateOtp() {
        return RandomStringUtils.randomNumeric(6);  // OTP is a 6-digit number
    }

    // Send OTP to the user's email
    private void sendOtpEmail(String email, String otp) {
        String subject = "Your Password Reset OTP";
        String messageType = "otp";
        mailService.sendEmail(email, subject, messageType, otp);
    }

    @Override
    public ResetPasswordRest verifyOtpAndResetPassword(String email, String otp, String newPassword, String confirmPassword) {
        // Verify OTP
        if (!otp.equals(otpMap.get(email))) {
            throw new RuntimeException("Invalid OTP.");
        }

        // Check if password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Password and confirm password do not match.");
        }

        // Find the user by email
        Optional<User> userOptional = userRepository.findUserByEmailOrPhone(email, email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        User user = userOptional.get();

        // Update the password
        user.setPassword(newPassword); // Set the new password
        userRepository.save(user); // Save the updated user

        // Clear OTP after successful reset
        otpMap.remove(email);

        // Send confirmation email
        mailService.sendEmail(user.getEmail(), "Password Reset Successful", "passwordReset", "");

        // Return success response
        ResetPasswordRest response = new ResetPasswordRest();
        response.setStatus("Success");
        response.setMessage("Password reset successfully.");
        return response;
    }
}
