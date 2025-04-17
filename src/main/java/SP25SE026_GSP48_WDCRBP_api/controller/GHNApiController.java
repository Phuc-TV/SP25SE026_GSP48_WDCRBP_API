package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CalculateFeeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateOrderGhnApiRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GetGHNAvailableServiceRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GetOrderInfoGhnApiRequest;
import SP25SE026_GSP48_WDCRBP_api.service.GHNApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/GHNApi")
public class GHNApiController {

    @Autowired
    private GHNApiService ghnApiService;

    @GetMapping("/provinces")
    public CoreApiResponse getProvinces() {
        return ghnApiService.getProvinces();
    }

    @GetMapping("/districts/{provinceId}")
    public CoreApiResponse getDistricts(@PathVariable int provinceId) {
        return ghnApiService.getDistricts(provinceId);
    }

    @GetMapping("/wards/{districtId}")
    public CoreApiResponse getWard(@PathVariable int districtId) {
        return ghnApiService.getWard(districtId);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/calculate-fee")
    public CoreApiResponse calculateShippingFee(@RequestBody CalculateFeeRequest request) {
        return ghnApiService.calculateShippingFee(request);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/services")
    public CoreApiResponse getWard(@RequestBody GetGHNAvailableServiceRequest request) {
        return ghnApiService.getAvailableService(request);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/service-order/{serviceOrderId}")
    public CoreApiResponse createServiceOrderShipmentGhnOrder(@PathVariable Long serviceOrderId ,@RequestBody CreateOrderGhnApiRequest request) {
        return ghnApiService.createOrder(serviceOrderId, request);
    }

    @PostMapping("/order-code")
    public CoreApiResponse createOrder(@RequestBody GetOrderInfoGhnApiRequest request) {
        return ghnApiService.getOrderInfo(request);
    }
}
