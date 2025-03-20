package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Configuration;
import SP25SE026_GSP48_WDCRBP_api.service.ConfigurationService;
import SP25SE026_GSP48_WDCRBP_api.service.GHNApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GHNApiServiceImpl implements GHNApiService {

    @Autowired
    private ConfigurationService configurationService;

    public GHNApiServiceImpl(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public CoreApiResponse getProvinces() {
        // Lấy cấu hình token từ database
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        String token = configurations.stream()
                .filter(config -> "GHN_Token_API".equals(config.getDescription()))
                .findFirst().get().getValue();

        // Tạo header với token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token); // Key header theo yêu cầu của API GHN

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Gọi API bên ngoài
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Chuyển chuỗi JSON trả về thành object (Map) để FE dễ truy xuất các trường riêng biệt
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
            return CoreApiResponse.success(responseMap, "successfully");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CoreApiResponse.error("Error parsing response");
        }
    }

    // API lấy danh sách District theo province_id
    @Override
    public CoreApiResponse getDistricts(int provinceId) {
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        String token = configurations.stream()
                .filter(config -> "GHN_Token_API".equals(config.getDescription()))
                .findFirst().get().getValue();

        // Tạo header với token và Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.set("Content-Type", "application/json");

        // Tạo body chứa province_id
        Map<String, Object> body = new HashMap<>();
        body.put("province_id", provinceId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Sử dụng HttpComponentsClientHttpRequestFactory để hỗ trợ GET có body
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Chuyển chuỗi JSON trả về thành Map để FE dễ lấy giá trị
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
            return CoreApiResponse.success(responseMap, "successfully");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CoreApiResponse.error("Error parsing response");
        }
    }

    @Override
    public CoreApiResponse getWard(int districtId) {
        // Lấy cấu hình token từ database
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        String token = configurations.stream()
                .filter(config -> "GHN_Token_API".equals(config.getDescription()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình GHN_Token_API"))
                .getValue();

        // Tạo header với token và Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.set("Content-Type", "application/json");

        // Tạo body chứa district_id
        Map<String, Object> body = new HashMap<>();
        body.put("district_id", districtId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Sử dụng HttpComponentsClientHttpRequestFactory để hỗ trợ POST có body
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        // URL theo tài liệu, sử dụng POST
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            return CoreApiResponse.success(responseMap, "successfully");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CoreApiResponse.error("Error parsing response");
        }
    }
}

