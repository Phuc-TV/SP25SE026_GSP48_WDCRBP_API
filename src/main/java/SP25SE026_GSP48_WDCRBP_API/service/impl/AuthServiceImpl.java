package SP25SE026_GSP48_WDCRBP_API.service.impl;

import SP25SE026_GSP48_WDCRBP_API.model.entity.AccessToken;
import SP25SE026_GSP48_WDCRBP_API.model.entity.RefreshToken;
import SP25SE026_GSP48_WDCRBP_API.model.entity.User;
import SP25SE026_GSP48_WDCRBP_API.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_API.model.requestModel.LoginRequest;
import SP25SE026_GSP48_WDCRBP_API.model.requestModel.SignupRequest;
import SP25SE026_GSP48_WDCRBP_API.model.responseModel.AuthenticationResponse;
import SP25SE026_GSP48_WDCRBP_API.repository.AccessTokenRepository;
import SP25SE026_GSP48_WDCRBP_API.repository.RefreshTokenRepository;
import SP25SE026_GSP48_WDCRBP_API.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_API.security.JwtTokenProvider;
import SP25SE026_GSP48_WDCRBP_API.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Override
    public AuthenticationResponse login(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmailOrPhone(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmailOrPhone(loginDto.getEmailOrPhone(), loginDto.getEmailOrPhone())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.BAD_REQUEST, "User not found"));
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

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

        // add check if username already exists
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
        }

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
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
        username = jwtTokenProvider.getUsernameFromJwt(refreshToken);
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.isTokenValid(refreshToken, userDetails.getUsername())
                    && !token.isRevoked() && !token.isExpired()
            ) {
                //map user to authentication
                Authentication userAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                var accessToken = jwtTokenProvider.generateAccessToken(userAuthentication);
                User user = this.userRepository.findByEmailOrPhone(username, username).orElseThrow();
                revokeAllUserAccessTokens(user);
                saveUserAccessToken(user, accessToken, token);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return null;
    }
}
