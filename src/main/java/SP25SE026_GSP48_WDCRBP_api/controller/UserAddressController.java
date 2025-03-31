package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UserAddressRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserAddressRes;
import SP25SE026_GSP48_WDCRBP_api.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/useraddresses")
@CrossOrigin(origins = "*")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping
    public CoreApiResponse<List<UserAddressRes>> getAllUserAddresses() {
        try {
            List<UserAddressRes> response = userAddressService.getAllUserAddresses();
            if (response == null || response.isEmpty()) {
                return CoreApiResponse.success(null, "Không có dữ liệu");
            }
            return CoreApiResponse.success(response, "Lấy danh sách địa chỉ thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể thực thi: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CoreApiResponse<?> getUserAddressById(@PathVariable Long id) {
        try {
            UserAddressRes response = userAddressService.getUserAddressById(id);
            if (response == null) {
                return CoreApiResponse.success(null, "Không có dữ liệu");
            }
            return CoreApiResponse.success(response, "Tìm kiếm địa chỉ thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể thực thi: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public CoreApiResponse<UserAddressRes> createUserAddress(@RequestBody UserAddressRequest userAddressRequest) {
        try {
            UserAddressRes response = userAddressService.createUserAddress(userAddressRequest);
            return CoreApiResponse.success(response, "Tạo địa chỉ thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể thực thi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public CoreApiResponse<UserAddressRes> updateUserAddress(@PathVariable Long id, @RequestBody UserAddressRequest userAddressRequest) {
         try{
                UserAddressRes response = userAddressService.updateUserAddress(id, userAddressRequest);
                 if (response == null) {
                     return CoreApiResponse.success(null, "Không có dữ liệu");
                 }
                return CoreApiResponse.success(response, "Cập nhật địa chỉ thành công");
         } catch (Exception e) {
                return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Không thể thực thi: " + e.getMessage());
         }
    }

    @DeleteMapping("/delete/{id}")
    public CoreApiResponse<String> deleteUserAddress(@PathVariable Long id) {
        userAddressService.deleteUserAddress(id);
        return CoreApiResponse.success("Xóa địa chỉ thành công");
    }
}
