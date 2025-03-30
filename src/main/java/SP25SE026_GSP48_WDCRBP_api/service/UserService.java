package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ChangePasswordRequest;

public interface UserService {

    CoreApiResponse getAllUser();

    CoreApiResponse getUserById(Long id);

    CoreApiResponse getUserInformationById(Long id);

    void changePassword(Long userId, ChangePasswordRequest request);
}
