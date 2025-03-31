package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UserAddressRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserAddressRes;

import java.util.List;

public interface UserAddressService {
    List<UserAddressRes> getAllUserAddresses();
    UserAddressRes getUserAddressById(Long id);
    UserAddressRes createUserAddress(UserAddressRequest userAddressRequest);
    UserAddressRes updateUserAddress(Long id, UserAddressRequest userAddressRequest);
    void deleteUserAddress(Long id);
}
