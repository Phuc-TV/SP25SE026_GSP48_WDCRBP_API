package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateUserProfileRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;

public interface UserService {

    CoreApiResponse getAllUser();

    CoreApiResponse getUserById(Long id);

    UserDetailRes getUserInformationById(Long id);

    void changePassword(Long userId, ChangePasswordRequest request);

    CoreApiResponse<?> updateUserProfile(UpdateUserProfileRequest request);
}
