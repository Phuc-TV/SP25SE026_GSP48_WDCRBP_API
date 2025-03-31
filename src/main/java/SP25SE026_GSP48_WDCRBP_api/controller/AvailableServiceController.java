package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AvailableServiceListItemRes;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/AvailableService")
public class AvailableServiceController {
    @Autowired
    private AvailableServiceService availableServiceService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAvailableServiceByWwId/{wwId}")
    public CoreApiResponse getAvailableServiceByWwId(@PathVariable Long wwId)
    {
        try {
            // Chuyển đổi danh sách dịch vụ thành danh sách đối tượng DTO
            List<AvailableServiceListItemRes> availableServiceList = availableServiceService.getAvailableServiceByWwId(wwId).stream()
                    .map(service -> modelMapper.map(service, AvailableServiceListItemRes.class))
                    .toList();

            return CoreApiResponse.success(availableServiceList);
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage(), null);
        }
    }
}
