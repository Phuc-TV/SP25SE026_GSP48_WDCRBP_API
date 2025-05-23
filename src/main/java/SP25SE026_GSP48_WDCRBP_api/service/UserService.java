package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserRes;
import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.BanUserRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateRoleRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateUserProfileRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;

import java.util.List;

public interface UserService {

    CoreApiResponse getAllUser();

    CoreApiResponse getUserById(Long id);

    UserDetailRes getUserInformationById(Long id);

    void changePassword(Long userId, ChangePasswordRequest request);

    CoreApiResponse<?> updateUserProfile(UpdateUserProfileRequest request);

    CoreApiResponse<?> updateUserRole(UpdateRoleRequest request);

    CoreApiResponse<?> banUser(BanUserRequest request);

    CoreApiResponse<List<UserRes>> getAllUsers();
}
