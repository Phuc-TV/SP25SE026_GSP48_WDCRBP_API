package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkerProfileDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ww")
public class WoodworkerProfileController {
    @Autowired
    private WoodworkerProfileService woodworkerProfileService;

    @Autowired
    private ModelMapper modelMapper;

    // Lấy danh sách tất cả các Woodworker với service pack "Gold", "Silver", "Bronze"
    @GetMapping("/listWW")
    public CoreApiResponse getAllWoodWorker() {
        List<WoodworkerProfileDto> ideas = woodworkerProfileService.getAllWoodWorker()
                .stream().map(idea -> modelMapper.map(idea, WoodworkerProfileDto.class))
                .toList();
        if (ideas == null || ideas.isEmpty()) {
            return CoreApiResponse.error("No data found");
        }

        return CoreApiResponse.success(ideas);
    }

    // Lấy thông tin của một Woodworker theo ID
    @GetMapping("/listWW/{wwId}")
    public CoreApiResponse getWoodworkerById(@PathVariable Long wwId) {
        return CoreApiResponse.success(modelMapper.map(
                woodworkerProfileService.getWoodworkerById(wwId), WoodworkerProfileDto.class
        ));
    }

    // Thêm một Service Pack cho Woodworker
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/addServicePack/{wwId}")
    public CoreApiResponse addServicePack(@PathVariable Long wwId, @RequestParam Long servicePackId) {
        WoodworkerProfileDto woodworkerProfileDto = modelMapper.map(
                woodworkerProfileService.addServicePack(servicePackId, wwId), WoodworkerProfileDto.class
        );
        return CoreApiResponse.success(woodworkerProfileDto);
    }

    @PostMapping("/ww-register")
    public CoreApiResponse registerWoodworker(@RequestBody @Valid WoodworkerRequest request) {
        try {
            WoodworkerProfileRest response = woodworkerProfileService.registerWoodworker(request);
            return CoreApiResponse.success(response, "Đăng ký thành công");
        } catch (Exception e) {
            return CoreApiResponse.error("form đăng ký có sai sót: " + e.getMessage());
        }
    }

    @PutMapping("/ww-update-status")
    public CoreApiResponse updateWoodworkerStatus(@RequestBody WoodworkerUpdateStatusRequest request) {
        try {
            WoodworkerUpdateStatusRest response = woodworkerProfileService.updateWoodworkerStatus(request);
            return CoreApiResponse.success(response, "Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return CoreApiResponse.error("Không thể cập nhật trạng thái: " + e.getMessage());
        }
    }

    @GetMapping("/listWW/inactive")
    public CoreApiResponse getInactiveWoodworkers() {
        List<ListRegisterRest.Data> result = woodworkerProfileService.getAllInactiveWoodworkers();
        if (result.isEmpty()) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND,"Danh sách không có thợ mộc nào chưa kích hoạt tài khoản");
        }
            return CoreApiResponse.success(result, "Danh sách thợ mộc chưa kích hoạt tài khoản");
    }

    @PutMapping("/addServicePack")
    public CoreApiResponse<UpdateWoodworkerServicePackRest> addServicePackToWoodworker(
            @Valid @RequestBody UpdateWoodworkerServicePackRequest request) {
        try {
            UpdateWoodworkerServicePackRest result = woodworkerProfileService.updateServicePackForWoodworker(request);
            return CoreApiResponse.success(result, "Cập nhật gói dịch vụ cho thợ mộc thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Thất bại khi cập nhật gói dịch vụ: " + e.getMessage());
        }
    }

}
