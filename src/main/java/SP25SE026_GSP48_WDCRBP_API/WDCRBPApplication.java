package SP25SE026_GSP48_WDCRBP_API;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@SpringBootApplication
public class WDCRBPApplication {
    public static void main(String[] args) {
        SpringApplication.run(WDCRBPApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép tất cả các nguồn truy cập, có thể thay đổi thành danh sách cụ thể nếu cần
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // Cho phép các phương thức HTTP cần thiết
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép tất cả các header
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Cho phép gửi cookie, header xác thực,...
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình cho tất cả các endpoint
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
