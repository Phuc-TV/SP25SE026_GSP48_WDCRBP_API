package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileListItemRes;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    // Lấy danh sách tất cả các Woodworker với service pack "Gold", "Silver", "Bronze"
    @GetMapping
    public CoreApiResponse<List<WoodworkerProfileListItemRes>> getAllWoodWorker() {
        try {
            List<WoodworkerProfileListItemRes> wwList = woodworkerProfileService.getAllWoodWorker();

            return CoreApiResponse.success(wwList);
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy danh sách xưởng mộc");
        }
    }

    // Lấy thông tin của một Woodworker theo ID
    @GetMapping("/{wwId}")
    public CoreApiResponse<WoodworkerProfileDetailRes> getWoodworkerById(@PathVariable Long wwId) {
        try {
            return CoreApiResponse.success(woodworkerProfileService.getWoodworkerById(wwId));
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy thông tin xưởng mộc");
        }
    }

    @GetMapping("/user/{userId}")
    public CoreApiResponse<WoodworkerProfileDetailRes> getWoodworkerProfileByUserId(@PathVariable Long userId) {
        try {
            return CoreApiResponse.success(woodworkerProfileService.getWoodworkerByUserId(userId));
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy thông tin xưởng mộc theo userId");
        }
    }

    // Thêm một Service Pack cho Woodworker
    @SecurityRequirement(name = "Bear Authentication")
    @PutMapping("/addServicePack")
    public CoreApiResponse<WoodworkerProfileListItemRes> addServicePack(@Valid @RequestBody UpdateWoodworkerServicePackRequest request) {
        try {
            WoodworkerProfileListItemRes woodworkerProfileListItemResponseDto = woodworkerProfileService.addServicePack(request.getServicePackId(), request.getWoodworkerId());

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
            return CoreApiResponse.success(result, "Danh sách xưởng mộc chưa kích hoạt tài khoản");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể lấy danh sách xưởng mộc chưa kích hoạt tài khoản: " + e.getMessage());
        }
    }

    @PutMapping("/update-public-status")
    public CoreApiResponse<UpdateStatusPublicRes> updatePublicStatus(@RequestBody @Valid UpdateStatusPublicRequest request) {
        try {
            UpdateStatusPublicRes response = woodworkerProfileService.updatePublicStatus(request);
            return CoreApiResponse.success(response, "Cập nhật trạng thái công khai thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể cập nhật trạng thái công khai: " + e.getMessage());
        }
    }

    @PutMapping("/update-woodworker-profile")
    public CoreApiResponse<WoodworkerProfileDetailRes> updateWoodworkerProfile(@RequestBody @Valid UpdateWoodworkerProfileRequest request) {
        try {
            WoodworkerProfileDetailRes updatedProfile = woodworkerProfileService.updateWoodworkerProfile(request);
            return CoreApiResponse.success(updatedProfile, "Cập nhật thông tin xưởng thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Cập nhật thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/update-warranty-policy")
    public CoreApiResponse<WoodworkerProfileDetailRes> updateWarrantyPolicy(@RequestBody @Valid UpdateWarrantyPolicyByWwIdRequest request) {
        try {
            WoodworkerProfileDetailRes updated = woodworkerProfileService.updateWarrantyPolicyByWwId(request);
            return CoreApiResponse.success(updated, "Cập nhật chính sách bảo hành thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể cập nhật chính sách bảo hành: " + e.getMessage());
        }
    }
}
