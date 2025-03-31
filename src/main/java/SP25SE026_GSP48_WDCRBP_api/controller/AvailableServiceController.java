package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.AvailableServiceUpdateReq;
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

    @GetMapping("/woodworker/{wwId}")
    public CoreApiResponse getAvailableServiceByWwId(@PathVariable Long wwId)
    {
        try {
            List<AvailableServiceListItemRes> availableServiceList = availableServiceService.getAvailableServiceByWwId(wwId).stream()
                    .map(service -> modelMapper.map(service, AvailableServiceListItemRes.class))
                    .toList();

            return CoreApiResponse.success(availableServiceList);
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage(), null);
        }
    }

    @PutMapping("/update")
    public CoreApiResponse updateAvailableService(@RequestBody AvailableServiceUpdateReq updateReq)
    {
        try {
            AvailableService availableService = availableServiceService.updateAvailableService(updateReq);

            if (availableService == null) {
                return CoreApiResponse.success(updateReq.getAvailableServiceId(), "Không tìm thấy dịch vụ với ID: " + updateReq.getAvailableServiceId());
            }

            return CoreApiResponse.success(modelMapper.map(availableService, AvailableServiceListItemRes.class));
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi khi cập nhật dịch vụ: " + e.getMessage(), null);
        }
    }
}
