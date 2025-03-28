package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackDetailsRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service-pack-details")
@CrossOrigin(origins = "*")
public class ServicePackDetailsController {

    @Autowired
    private ServicePackDetailsService service;

    @PostMapping("/create")
    public CoreApiResponse<CreateServicePackDetailsRest> create(@Valid @RequestBody CreateServicePackDetailsRequest request) {
        CreateServicePackDetailsRest response = service.createServicePackDetails(request);
        try{
            return CoreApiResponse.success(response, "Tạo Service Pack Details thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo Service Pack Details thất bại: " + e.getMessage());
        }

    }

    @PutMapping("/update")
    public CoreApiResponse<CreateServicePackDetailsRest> updateServicePackDetails(
            @RequestParam Long servicePackDetailsId,
            @Valid @RequestBody CreateServicePackDetailsRequest request) {
        CreateServicePackDetailsRest response = service.updateServicePackDetails(servicePackDetailsId, request);
        try{
            return CoreApiResponse.success(response, "Sửa chữa Service Pack Details thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo Service Pack Details thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public CoreApiResponse<?> deleteServicePackDetails(
            @RequestParam("servicePackDetailsId") Long servicePackDetailsId) {
        service.deleteServicePackDetails(servicePackDetailsId);
        try{
            return CoreApiResponse.success("Xóa Service Pack thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Ko thể xóa: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public CoreApiResponse<ListServicePackDetailsRest> getAllServicePackDetails() {
        ListServicePackDetailsRest response = service.getAllServicePackDetails();
        try{
            return CoreApiResponse.success(response, "Lấy danh sách Service Pack Details thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy danh sách Service Pack Details thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    public CoreApiResponse<ListServicePackDetailsRest> getServicePackDetailsById(
            @RequestParam("servicePackDetailsId") Long servicePackDetailsId) {
        ListServicePackDetailsRest response = service.getServicePackDetailsById(servicePackDetailsId);
        try{
            return CoreApiResponse.success(response, "Lấy thông tin Service Pack Details thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy thông tin Service Pack Details thất bại: " + e.getMessage());
        }
    }
}

