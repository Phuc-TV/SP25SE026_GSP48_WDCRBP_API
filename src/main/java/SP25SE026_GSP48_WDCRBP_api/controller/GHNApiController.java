package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CalculateFeeRequest;
import SP25SE026_GSP48_WDCRBP_api.service.GHNApiService;
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

    @PostMapping("/calculate-fee")
    public CoreApiResponse calculateShippingFee(@RequestBody CalculateFeeRequest request) {
        return ghnApiService.calculateShippingFee(request);
    }
}
