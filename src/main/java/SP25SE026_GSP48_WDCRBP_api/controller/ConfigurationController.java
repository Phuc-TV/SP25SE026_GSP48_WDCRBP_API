package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationNameRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationSearchRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationUpsertRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ConfigurationRes;
import SP25SE026_GSP48_WDCRBP_api.service.ConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuration")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping("/getAll")
    public CoreApiResponse<List<ConfigurationRes>> getAllConfigurations() {
        try {
            List<ConfigurationRes> result = configurationService.getAll();
            return CoreApiResponse.success(result, "Lấy tất cả cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách cấu hình: " + e.getMessage());
        }
    }

    @PostMapping("/getByDescription")
    public CoreApiResponse<List<ConfigurationRes>> getByDescription(
            @Valid @RequestBody ConfigurationSearchRequest request) {
        try {
            List<ConfigurationRes> result = configurationService.getByDescription(request);
            return CoreApiResponse.success(result, "Tìm cấu hình theo mô tả thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tìm cấu hình theo mô tả: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public CoreApiResponse<ConfigurationRes> create(
            @Valid @RequestBody ConfigurationUpsertRequest request) {
        try {
            ConfigurationRes created = configurationService.create(request);
            return CoreApiResponse.success(created, "Tạo cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tạo cấu hình: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public CoreApiResponse<ConfigurationRes> update(
            @Valid @RequestBody ConfigurationUpsertRequest request) {
        try {
            ConfigurationRes updated = configurationService.update(request);
            return CoreApiResponse.success(updated, "Cập nhật cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật cấu hình: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public CoreApiResponse<Void> delete(@PathVariable Long id) {
        try {
            configurationService.delete(id);
            return CoreApiResponse.success("Xóa cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi xóa cấu hình: " + e.getMessage());
        }
    }

    @PostMapping("/getByName")
    public CoreApiResponse<ConfigurationRes> getConfigByName(@Valid @RequestBody ConfigurationNameRequest request) {
        try {
            String value = configurationService.getValue(request.getName());
            ConfigurationRes res = new ConfigurationRes();
            res.setName(request.getName());
            res.setValue(value);
            return CoreApiResponse.success(res, "Lấy giá trị cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy cấu hình: " + e.getMessage());
        }
    }

    @PutMapping("/updateValue")
    public CoreApiResponse<ConfigurationRes> updateConfigValue(@Valid @RequestBody ConfigurationUpdateRequest request) {
        try {
            ConfigurationRes updated = configurationService.update(request);
            return CoreApiResponse.success(updated, "Cập nhật giá trị cấu hình thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật cấu hình: " + e.getMessage());
        }
    }
}
