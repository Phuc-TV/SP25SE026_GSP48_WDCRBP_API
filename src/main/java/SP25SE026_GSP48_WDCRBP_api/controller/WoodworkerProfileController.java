package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileListItemRes;
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
    @GetMapping
    public CoreApiResponse<List<WoodworkerProfileListItemRes>> getAllWoodWorker() {
        List<WoodworkerProfileListItemRes> wwList = woodworkerProfileService.getAllWoodWorker()
                .stream().map(idea -> modelMapper.map(idea, WoodworkerProfileListItemRes.class))
                .toList();

        return CoreApiResponse.success(wwList);
    }

    // Lấy thông tin của một Woodworker theo ID
    @GetMapping("/{wwId}")
    public CoreApiResponse<WoodworkerProfileDetailRes> getWoodworkerById(@PathVariable Long wwId) {
        return CoreApiResponse.success(modelMapper.map(
                woodworkerProfileService.getWoodworkerById(wwId), WoodworkerProfileDetailRes.class
        ));
    }

    @GetMapping("/user/{userId}")
    public CoreApiResponse<WoodworkerProfileDetailRes> getWoodworkerProfileByUserId(@PathVariable Long userId) {
        return CoreApiResponse.success(modelMapper.map(
                woodworkerProfileService.getWoodworkerByUserId(userId), WoodworkerProfileDetailRes.class
        ));
    }

    // Thêm một Service Pack cho Woodworker
    @SecurityRequirement(name = "Bear Authentication")
    @PutMapping("/addServicePack")
    public CoreApiResponse<WoodworkerProfileListItemRes> addServicePack(@Valid @RequestBody UpdateWoodworkerServicePackRequest request) {
        try {
            WoodworkerProfileListItemRes woodworkerProfileListItemResponseDto = modelMapper.map(
                    woodworkerProfileService.addServicePack(request.getServicePackId(), request.getWoodworkerId()), WoodworkerProfileListItemRes.class
            );

            return CoreApiResponse.success(woodworkerProfileListItemResponseDto);
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Thêm gói dịch vụ thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/ww-register")
    public CoreApiResponse<WoodworkerProfileRes> registerWoodworker(@RequestBody @Valid WoodworkerRequest request) {
        try {
            WoodworkerProfileRes response = woodworkerProfileService.registerWoodworker(request);
            return CoreApiResponse.success(response, "Đăng ký thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "form đăng ký có sai sót: " + e.getMessage());
        }
    }

    @PutMapping("/ww-update-status")
    public CoreApiResponse<WoodworkerUpdateStatusRes> updateWoodworkerStatus(@RequestBody WoodworkerUpdateStatusRequest request) {
        try {
            WoodworkerUpdateStatusRes response = woodworkerProfileService.updateWoodworkerStatus(request);
            return CoreApiResponse.success(response, "Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể cập nhật trạng thái: " + e.getMessage());
        }
    }

    @GetMapping("/inactive")
    public CoreApiResponse<List<ListRegisterRes.Data>> getInactiveWoodworkers() {
        List<ListRegisterRes.Data> result = woodworkerProfileService.getAllInactiveWoodworkers();
       try{
            return CoreApiResponse.success(result, "Danh sách thợ mộc chưa kích hoạt tài khoản");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể lấy danh sách thợ mộc chưa kích hoạt tài khoản: " + e.getMessage());
        }
    }
}
