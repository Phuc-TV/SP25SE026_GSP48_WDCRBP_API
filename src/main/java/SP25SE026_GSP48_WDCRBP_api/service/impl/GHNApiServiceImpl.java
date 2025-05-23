package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ItemDTO;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Configuration;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CalculateFeeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateOrderGhnApiRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GetGHNAvailableServiceRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GetOrderInfoGhnApiRequest;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ShipmentRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ConfigurationService;
import SP25SE026_GSP48_WDCRBP_api.service.GHNApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GHNApiServiceImpl implements GHNApiService {

    private final ConfigurationService configurationService;
    private final ShipmentRepository shipmentRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    @Autowired
    public GHNApiServiceImpl(ConfigurationService configurationService, ShipmentRepository shipmentRepository, ServiceOrderRepository serviceOrderRepository) {
        this.configurationService = configurationService;
        this.shipmentRepository = shipmentRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    private String getTokenFromConfig() {
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        return configurations.stream()
                .filter(config -> "GHN_Token_API".equals(config.getDescription()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình GHN_Token_API"))
                .getValue();
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", getTokenFromConfig());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public CoreApiResponse getProvinces() {
        HttpEntity<?> entity = new HttpEntity<>(getDefaultHeaders());
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
        return callExternalAPI(url, HttpMethod.GET, entity);
    }

    @Override
    public CoreApiResponse getDistricts(int provinceId) {
        Map<String, Object> body = new HashMap<>();
        body.put("province_id", provinceId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, getDefaultHeaders());

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    @Override
    public CoreApiResponse getWard(int districtId) {
        Map<String, Object> body = new HashMap<>();
        body.put("district_id", districtId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, getDefaultHeaders());

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    @Override
    public CoreApiResponse calculateShippingFee(CalculateFeeRequest request) {
        // Tính toán từ danh sách items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return CoreApiResponse.error("Danh sách sản phẩm không được để trống");
        }

        int totalLength = 0;
        int maxWidth = 0;
        int maxHeight = 0;
        int totalWeight = 0;

        for (ItemDTO item : request.getItems()) {
            if (item.getWeight() == 0) {
                int estimatedWeight = estimateWeight(item.getLength(), item.getWidth(), item.getHeight());
                item.setWeight(Math.min(estimatedWeight, 50000));
                item.setHeight(Math.min(item.getHeight(), 150));
                item.setWidth(Math.min(item.getWidth(), 150));
                item.setLength(Math.min(item.getLength(), 150));
            }

            totalLength += item.getLength();
            maxWidth = Math.max(maxWidth, item.getWidth());
            maxHeight = Math.max(maxHeight, item.getHeight());
            totalWeight += item.getWeight() * item.getQuantity();
        }

        request.setLength(Math.min(totalLength, 150));
        request.setWidth(Math.min(maxWidth, 150));
        request.setHeight(Math.min(maxHeight, 150));
        request.setWeight(Math.min(totalWeight, 50000));

        HttpHeaders headers = getDefaultHeaders();
        headers.set("ShopId", getShopIdFromConfig()); // có thể cho vào config DB nếu muốn linh hoạt

        HttpEntity<CalculateFeeRequest> entity = new HttpEntity<>(request, headers);

        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    @Override
    public CoreApiResponse getAvailableService(GetGHNAvailableServiceRequest request) {
        HttpHeaders headers = getDefaultHeaders();
        request.setShop_id(Integer.parseInt(getShopIdFromConfig()));

        HttpEntity<GetGHNAvailableServiceRequest> entity = new HttpEntity<>(request, headers);

        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";

        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    private CoreApiResponse callExternalAPI(String url, HttpMethod method, HttpEntity<?> entity) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            return CoreApiResponse.success(responseMap, "Thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return CoreApiResponse.error("Lỗi gọi API GHN: " + e.getMessage());
        }
    }

    private int estimateWeight(int length, int width, int height) {
        double density = 0.65; // g/cm³, gỗ đặc trung bình
        double volume = length * width * height; // cm³
        return (int) Math.round(volume * density); // gram
    }

    @Override
    public CoreApiResponse createOrder(Long serviceOrderId, CreateOrderGhnApiRequest request) {
        HttpHeaders headers = getDefaultHeaders();
        headers.set("ShopId", getShopIdFromConfig()); // Lưu ý: Bạn có thể lấy ShopId từ DB (ConfigurationService)

        // Tính toán từ danh sách items
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return CoreApiResponse.error("Danh sách sản phẩm không được để trống");
        }

        int totalLength = 0;
        int maxWidth = 0;
        int maxHeight = 0;
        int totalWeight = 0;

        for (CreateOrderGhnApiRequest.OrderItem item : request.getItems()) {
            if (item.getWeight() == 0) {
                int estimatedWeight = estimateWeight(item.getLength(), item.getWidth(), item.getHeight());
                item.setWeight(Math.min(estimatedWeight, 50000));
                item.setHeight(Math.min(item.getHeight(), 150));
                item.setWidth(Math.min(item.getWidth(), 150));
                item.setLength(Math.min(item.getLength(), 150));
            }

            totalLength += item.getLength();
            maxWidth = Math.max(maxWidth, item.getWidth());
            maxHeight = Math.max(maxHeight, item.getHeight());
            totalWeight += item.getWeight() * item.getQuantity();
        }

        request.setRequired_note("CHOXEMHANGKHONGTHU");
        request.setLength(Math.min(totalLength, 150));
        request.setWidth(Math.min(maxWidth, 150));
        request.setHeight(Math.min(maxHeight, 150));
        request.setWeight(Math.min(totalWeight, 50000));

        HttpEntity<CreateOrderGhnApiRequest> entity = new HttpEntity<>(request, headers);
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    @Override
    public CoreApiResponse getOrderInfo(GetOrderInfoGhnApiRequest request) {
        HttpHeaders headers = getDefaultHeaders();

        HttpEntity<GetOrderInfoGhnApiRequest> entity = new HttpEntity<>(request, headers);
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";

        return callExternalAPI(url, HttpMethod.POST, entity);
    }

    private String getShopIdFromConfig() {
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        return configurations.stream()
                .filter(config -> "GHN_Shop_ID".equals(config.getDescription()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình GHN_Shop_ID"))
                .getValue();
    }
}
