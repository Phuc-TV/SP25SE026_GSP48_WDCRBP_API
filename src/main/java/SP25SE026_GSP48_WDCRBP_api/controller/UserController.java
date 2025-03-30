package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public CoreApiResponse<?> getUserInformationById(@PathVariable Long id)
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
}
