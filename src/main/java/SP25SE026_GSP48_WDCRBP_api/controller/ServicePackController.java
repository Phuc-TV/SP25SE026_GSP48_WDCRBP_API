package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRes;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-pack")
@CrossOrigin(origins = "*")
public class ServicePackController {

    @Autowired
    private ServicePackService servicePackService;

    @PostMapping("/create")
    public CoreApiResponse<CreateServicePackRes> createServicePack(
            @Valid @RequestBody CreateServicePackRequest request) {
        try{
            return CoreApiResponse.success(servicePackService.createServicePack(request), "Tạo Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo Service Pack thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public CoreApiResponse<CreateServicePackRes> updateServicePack(
            @RequestParam Long servicePackId,
            @Valid @RequestBody CreateServicePackRequest request
    ) {
        try{
            return CoreApiResponse.success(servicePackService.updateServicePack(servicePackId, request), "Sửa đổi Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Sửa đổi Service Pack thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public CoreApiResponse<?> deleteServicePack(@RequestParam Long servicePackId) {
        servicePackService.deleteServicePack(servicePackId);
        try{
            return CoreApiResponse.success("Xóa Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Ko thể xóa: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public CoreApiResponse<List<ListServicePackRes.Data>> getAllServicePacks() {
        List<ListServicePackRes.Data> result = servicePackService.getAllServicePacks();

        if (result.isEmpty()) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Danh sách Service Pack trống");
        }

        return CoreApiResponse.success(result, "Lấy danh sách Service Pack thành công");
    }

    @GetMapping("/listActive")
    public CoreApiResponse<List<ListServicePackRes.Data>> getAllActiveServicePacks() {
        List<ListServicePackRes.Data> result = servicePackService.getAllActiveServicePacks();

        if (result.isEmpty()) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Danh sách Service Pack trống");
        }

        return CoreApiResponse.success(result, "Lấy danh sách Service Pack thành công");
    }

    @GetMapping("/detail")
    public CoreApiResponse<ListServicePackRes> getServicePackById(@RequestParam Long servicePackId) {
        try {
            ListServicePackRes response = servicePackService.getServicePackById(servicePackId);

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new RuntimeException("Service Pack not found with ID: " + servicePackId);
            }

            return CoreApiResponse.success(response, "Lấy thông tin Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Lấy thông tin Service Pack thất bại: " + e.getMessage());
        }
    }


}
