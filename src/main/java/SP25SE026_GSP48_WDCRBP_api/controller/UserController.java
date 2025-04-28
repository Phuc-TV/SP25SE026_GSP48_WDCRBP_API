package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.BanUserRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateRoleRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateUserProfileRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserRes;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/getUserInformationById/{id}")
    public CoreApiResponse<UserDetailRes> getUserInformationById(@PathVariable Long id)
    {
        return CoreApiResponse.success(userService.getUserInformationById(id));
    }

    @PostMapping("/change-password/{userId}")
    public CoreApiResponse<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        try {
            userService.changePassword(userId, request);
            return CoreApiResponse.success("Đã thay đổi mật khẩu thành công");
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Đã thay đổi mật khẩu thất bại" + e.getMessage());
        }
    }

    @PostMapping("/update-profile")
    public CoreApiResponse<?> updateUserProfile(@RequestBody @Valid UpdateUserProfileRequest request) {
        try {
            return userService.updateUserProfile(request);
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public CoreApiResponse<List<UserRes>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/update-role")
    public CoreApiResponse<?> updateRole(@RequestBody @Valid UpdateRoleRequest request) {
        try {
            return userService.updateUserRole(request);
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Update role failed: " + e.getMessage());
        }
    }

    @PostMapping("/ban-user")
    public CoreApiResponse<?> banUser(@RequestBody @Valid BanUserRequest request) {
        try {
            return userService.banUser(request);
        } catch (RuntimeException e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Ban user failed: " + e.getMessage());
        }
    }

}
