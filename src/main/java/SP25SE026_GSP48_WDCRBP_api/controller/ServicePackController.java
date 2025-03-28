package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service-pack")
@CrossOrigin(origins = "*")
public class ServicePackController {

    @Autowired
    private ServicePackService servicePackService;

    @PostMapping("/create")
    public CoreApiResponse<CreateServicePackRest> createServicePack(
            @Valid @RequestBody CreateServicePackRequest request) {
        try{
            return CoreApiResponse.success(servicePackService.createServicePack(request), "Tạo Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo Service Pack thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public CoreApiResponse<CreateServicePackRest> updateServicePack(
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
    public CoreApiResponse<ListServicePackRest> getAllServicePacks() {
        ListServicePackRest response = servicePackService.getAllServicePacks();
        try{
            return CoreApiResponse.success(response, "Lấy danh sách Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy danh sách Service Pack thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    public CoreApiResponse<ListServicePackRest> getServicePackById(@RequestParam Long servicePackId) {
        ListServicePackRest response = servicePackService.getServicePackById(servicePackId);
        try{
            return CoreApiResponse.success(response, "Lấy thông tin Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy thông tin Service Pack thất bại: " + e.getMessage());
        }
    }

}
