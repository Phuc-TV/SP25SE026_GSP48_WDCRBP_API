package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;

public interface UserService {
    CoreApiResponse getAllUser();

    CoreApiResponse getUserById(Long id);
}
