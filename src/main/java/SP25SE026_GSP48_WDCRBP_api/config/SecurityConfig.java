package SP25SE026_GSP48_WDCRBP_api.config;

import SP25SE026_GSP48_WDCRBP_api.security.JwtAuthenticationEntryPoint;
import SP25SE026_GSP48_WDCRBP_api.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private LogoutHandler logoutHandler;

    @Autowired
    public SecurityConfig(@Lazy JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Lazy LogoutHandler logoutHandler) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        // ✅ Swagger UI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/"
                        ).permitAll()

                        // ✅ Public APIs
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/OTP/start-verification").permitAll()
                        .requestMatchers("/api/v1/OTP/check-verification").permitAll()
                        .requestMatchers("/api/v1/GHNApi/**").permitAll()
                        .requestMatchers("api/v1/payment/**").permitAll()
                        .requestMatchers("/api/v1/transaction/**").permitAll()
                        .requestMatchers("/api/v1/ww/**").permitAll()
                        .requestMatchers("/api/v1/service-pack/**").permitAll()
                        .requestMatchers("/api/v1/service-pack-details/**").permitAll()
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/meet/**").permitAll()
                        .requestMatchers("/api/v1/designIdea/**").permitAll()
                        .requestMatchers("/api/v1/wallet/**").permitAll()
                        .requestMatchers("/api/v1/decrypt/**").permitAll()
                        .requestMatchers("/api/v1/AvailableService/**").permitAll()
                        .requestMatchers("/api/v1/Category/**").permitAll()
                        .requestMatchers("/api/v1/products/**").permitAll()
                        .requestMatchers("/api/v1/posts/**").permitAll()
                        .requestMatchers("/api/v1/useraddresses/**").permitAll()
                        .requestMatchers("/api/v1/reviews/**").permitAll()
                        .requestMatchers("/api/v1/service-orders/**").permitAll()
                        .requestMatchers("/api/v1/tech-spec/**").permitAll()
                        .requestMatchers("/api/v1/Contract/**").permitAll()
                        .requestMatchers("/api/v1/OrderDeposit/**").permitAll()
                        .requestMatchers("/api/v1/OderProgress/**").permitAll()
                        .requestMatchers("/api/v1/Shipment/**").permitAll()
                        .requestMatchers("/api/v1/complaints/**").permitAll()
                        .requestMatchers("/api/v1/quotation/**").permitAll()
                        .requestMatchers("/api/v1/configuration/**").permitAll()
                        .requestMatchers("/api/v1/guarantee-orders/**").permitAll()
                        .requestMatchers("/api/v1/service-deposits/**").permitAll()

                        // ❗ Tất cả route còn lại cần auth
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.logout((logout) -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        );

        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
