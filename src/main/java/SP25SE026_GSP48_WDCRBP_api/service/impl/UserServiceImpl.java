package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserInfoResponse;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import SP25SE026_GSP48_WDCRBP_api.util.AESUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

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
    public CoreApiResponse getUserInformationById(Long id)
    {
        return CoreApiResponse.success(modelMapper.map(userRepository.findById(id), UserInfoResponse.class), "successfully");
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        try {
            String AES_KEY = "YourSecretKey123";

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            String decryptedPassword = AESUtil.decrypt(user.getPassword(), AES_KEY);
            if (!decryptedPassword.equals(request.getOldPassword())) {
                throw new RuntimeException("Mật khẩu cũ không đúng");
            }

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("Mật khẩu mới và mật khẩu xác nhận không khớp");
            }

            String encryptedNewPassword = AESUtil.encrypt(request.getNewPassword(), AES_KEY);
            user.setPassword(encryptedNewPassword);
            userRepository.save(user);

        } catch (RuntimeException e) {
            throw e; // Rethrow so controller can catch
        } catch (Exception e) {
            throw new RuntimeException("Thay đổi mật khẩu không thành công: " + e.getMessage(), e);
        }
    }
}
