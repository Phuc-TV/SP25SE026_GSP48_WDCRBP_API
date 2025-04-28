package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.BanUserRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateRoleRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateUserProfileRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserInfoResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserRes;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public CoreApiResponse getAllUser()
    {
        return CoreApiResponse.success(userRepository.findAll(), "successfully");
    }

    @Override
    public CoreApiResponse getUserById(Long id)
    {
        return CoreApiResponse.success(userRepository.findById(id), "successfully");
    }

    @Override
    public UserDetailRes getUserInformationById(Long id)
    {
        return modelMapper.map(userRepository.findById(id), UserDetailRes.class);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Check if old password matches the stored hashed password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và mật khẩu xác nhận không khớp");
        }

        // Hash the new password before saving
        String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    @Override
    public CoreApiResponse<?> updateUserProfile(UpdateUserProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + request.getUserId()));

        user.setUsername(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return CoreApiResponse.success("Cập nhật thông tin người dùng thành công");
    }

    @Override
    public CoreApiResponse<?> updateUserRole(UpdateRoleRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        user.setRole(request.getNewRole());
        userRepository.save(user);

        return CoreApiResponse.success("Role updated successfully");
    }

    @Override
    public CoreApiResponse<?> banUser(BanUserRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        user.setStatus(!request.getBanned());
        userRepository.save(user);

        return CoreApiResponse.success("User ban status updated successfully");
    }

    @Override
    public CoreApiResponse<List<UserRes>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserRes> userResponses = users.stream().map(user -> {
            UserRes userRes = new UserRes();
            userRes.setId(user.getUserId());
            userRes.setUsername(user.getUsername());
            userRes.setEmail(user.getEmail());
            userRes.setPhone(user.getPhone());
            userRes.setRole(user.getRole());
            userRes.setStatus(user.getStatus());
            return userRes;
        }).collect(Collectors.toList());

        return CoreApiResponse.success(userResponses, "Successfully fetched all users");
    }
}
