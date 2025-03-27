package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/AvailableService")
public class AvailableServiceController {
    @Autowired
    private AvailableServiceService availableServiceService;

    @GetMapping("/getAvailableServiceByWwId/{wwId}")
    public CoreApiResponse getAvailableServiceByWwId(@PathVariable Long wwId)
    {
        List<AvailableService> availableServices = availableServiceService.getAvailableServiceByWwId(wwId);

        if (availableServices == null)
            return CoreApiResponse.error("Empty list");
        return CoreApiResponse.success(availableServices);
    }
}
