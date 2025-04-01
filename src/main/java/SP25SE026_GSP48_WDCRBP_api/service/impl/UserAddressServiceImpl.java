package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.UserAddress;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UserAddressRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserAddressRes;
import SP25SE026_GSP48_WDCRBP_api.repository.UserAddressRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    public List<UserAddressRes> getAllUserAddresses() {
        List<UserAddress> addresses = userAddressRepository.findAll();
        return addresses.stream()
                .map(this::convertToUserAddressRes)
                .collect(Collectors.toList());
    }

    @Override
    public UserAddressRes getUserAddressById(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));
        return convertToUserAddressRes(userAddress);
    }

    @Override
    public UserAddressRes createUserAddress(UserAddressRequest userAddressRequest) {
        var user = userRepository.findById(userAddressRequest.getUserId())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found"));

        // Check if the user already has 3 addresses
        List<UserAddress> existingAddresses = userAddressRepository.findAll()
                .stream()
                .filter(addr -> addr.getUser().getUserId().equals(user.getUserId()))
                .collect(Collectors.toList());

        if (existingAddresses.size() >= 3) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Mỗi người dùng chỉ được phép có tối đa 3 địa chỉ.");
        }

        // If this new address is set as default, unset others
        if (userAddressRequest.getIsDefault()) {
            existingAddresses.forEach(addr -> addr.setIsDefault(false));
            userAddressRepository.saveAll(existingAddresses);
        }

        // Create new address
        UserAddress userAddress = UserAddress.builder()
                .isDefault(userAddressRequest.getIsDefault())
                .address(userAddressRequest.getAddress())
                .wardCode(userAddressRequest.getWardCode())
                .districtId(userAddressRequest.getDistrictId())
                .cityId(userAddressRequest.getCityId())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        UserAddress saved = userAddressRepository.save(userAddress);
        return convertToUserAddressRes(saved);
    }

    @Override
    public UserAddressRes updateUserAddress(Long id, UserAddressRequest userAddressRequest) {
        UserAddress existing = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));

        var user = userRepository.findById(userAddressRequest.getUserId())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found"));

        // If updating to default, unset others first
        if (Boolean.TRUE.equals(userAddressRequest.getIsDefault())) {
            List<UserAddress> userAddresses = userAddressRepository.findByUser_UserId(user.getUserId());
            for (UserAddress address : userAddresses) {
                if (!address.getUserAddressId().equals(existing.getUserAddressId())) {
                    address.setIsDefault(false);
                    userAddressRepository.save(address);
                }
            }
        }

        existing.setIsDefault(userAddressRequest.getIsDefault());
        existing.setAddress(userAddressRequest.getAddress());
        existing.setWardCode(userAddressRequest.getWardCode());
        existing.setDistrictId(userAddressRequest.getDistrictId());
        existing.setCityId(userAddressRequest.getCityId());
        existing.setUser(user);

        UserAddress updated = userAddressRepository.save(existing);
        return convertToUserAddressRes(updated);
    }

    @Override
    public void deleteUserAddress(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));
        userAddressRepository.delete(userAddress);
    }

    private UserAddressRes convertToUserAddressRes(UserAddress entity) {
        return UserAddressRes.builder()
                .userAddressId(entity.getUserAddressId())
                .isDefault(entity.getIsDefault())
                .address(entity.getAddress())
                .wardCode(entity.getWardCode())
                .districtId(entity.getDistrictId())
                .cityId(entity.getCityId())
                .createdAt(entity.getCreatedAt())
                .userId(entity.getUser() != null ? entity.getUser().getUserId() : null)
                .build();
    }
}
