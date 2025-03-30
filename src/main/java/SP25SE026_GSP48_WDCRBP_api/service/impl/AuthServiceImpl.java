package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AccessToken;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RefreshToken;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Wallet;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.LoginOtpRest;
import SP25SE026_GSP48_WDCRBP_api.repository.AccessTokenRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.RefreshTokenRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WalletRepository;
import SP25SE026_GSP48_WDCRBP_api.security.JwtTokenProvider;
import SP25SE026_GSP48_WDCRBP_api.service.AuthService;
import SP25SE026_GSP48_WDCRBP_api.util.AESUtil;
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
    private MailServiceImpl mailServiceImpl;

    @Autowired
    private WalletRepository walletRepository;

    private static final Map<String, String> otpMap = new HashMap<>();

    @Override
    public AuthenticationResponse login(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmailOrPhone(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findUserByEmailOrPhone(loginDto.getEmailOrPhone(), loginDto.getEmailOrPhone())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản"));

        if(user.getStatus().equals(false)) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Tài khoản chưa được kích hoạt");
        }

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
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Đã có tài khoản sử dụng email đó rồi!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        user.setRole("Customer");

        user.setStatus(false);

        String otp = generateOtp();
        user.setOTP(otp);
        userRepository.save(user);

        sendOtpEmail(user.getEmail(), otp);

        return "Tài khoản đã được đăng ký thành công!";
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Token không đúng định dạng");
        }

        String refreshToken = authHeader.substring(7);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy refresh token"));

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

        throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Refresh token không hợp lệ");
    }

    @Override
    public void sendOtpToEmail(String email) {
        Optional<User> userOptional = userRepository.findUserByEmailOrPhone(email, email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        String otp = generateOtp();
        user.setOTP(otp);
        userRepository.save(user);
        sendOtpEmail(user.getEmail(), otp);
    }

    private String generateOtp() {
        return RandomStringUtils.randomNumeric(6);  // OTP is a 6-digit number
    }
    private void sendOtpEmail(String email, String otp) {
        String subject = "[WDCRBP] Mã OTP của bạn";
        String messageType = "otp";
        mailServiceImpl.sendEmail(email, subject, messageType, otp);
    }

    @Override
    public LoginOtpRest loginWithOtp(LoginOtpRequest request) {
        Optional<User> userOptional = userRepository.findUserByEmailOrPhone(request.getEmail(), request.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        if (user.getStatus() == false) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }
        if (user.getOTP() == null || !user.getOTP().equals(request.getOtp())) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }
        user.setOTP(null);
        userRepository.save(user);
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        revokeRefreshToken(accessToken);
        RefreshToken savedRefreshToken = saveUserRefreshToken(refreshToken);
        revokeAllUserAccessTokens(user);
        saveUserAccessToken(user, accessToken, savedRefreshToken);
        return new LoginOtpRest(accessToken, refreshToken);
    }

    @Override
    public void otpChangeStatusAccount(LoginOtpRequest request) {
        Optional<User> userOptional = userRepository.findUserByEmailOrPhone(request.getEmail(), request.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOptional.get();
        if (user.getOTP() == null || !user.getOTP().equals(request.getOtp())) {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }
        user.setStatus(true);
        user.setOTP(null);
        userRepository.save(user);
        if (walletRepository.findAll().stream().noneMatch(w -> w.getUser().getUserId().equals(user.getUserId()))) {
            Wallet wallet = Wallet.builder()
                    .user(user)
                    .balance(0f)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            walletRepository.save(wallet);
        }
    }

    @Override
    public void resetPasswordWithOTP(String email, String otp, ResetPasswordOTPRequest request) {
        User user = userRepository.findUserByEmailOrPhone(email, email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này."));

        if (user.getOTP() == null || !user.getOTP().equals(otp)) {
            throw new RuntimeException("OTP không chính xác hoặc đã hết hạn.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp.");
        }

        // 👉 Use hash instead of encryption
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(hashedPassword);
        user.setOTP(null); // clear OTP after use
        userRepository.save(user);
    }
}
