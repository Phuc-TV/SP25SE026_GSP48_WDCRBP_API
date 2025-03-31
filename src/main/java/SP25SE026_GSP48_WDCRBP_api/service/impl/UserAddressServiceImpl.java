package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.UserAddress;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UserAddressRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserAddressRes;
import SP25SE026_GSP48_WDCRBP_api.repository.UserAddressRepository;
import SP25SE026_GSP48_WDCRBP_api.service.UserAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserAddressRes> getAllUserAddresses() {
        List<UserAddress> addresses = userAddressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, UserAddressRes.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserAddressRes getUserAddressById(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));
        return modelMapper.map(userAddress, UserAddressRes.class);
    }

    @Override
    public UserAddressRes createUserAddress(UserAddressRequest userAddressRequest) {
        UserAddress userAddress = modelMapper.map(userAddressRequest, UserAddress.class);
        UserAddress savedUserAddress = userAddressRepository.save(userAddress);
        return modelMapper.map(savedUserAddress, UserAddressRes.class);
    }

    @Override
    public UserAddressRes updateUserAddress(Long id, UserAddressRequest userAddressRequest) {
        UserAddress existingUserAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));

        modelMapper.map(userAddressRequest, existingUserAddress);
        UserAddress updatedUserAddress = userAddressRepository.save(existingUserAddress);
        return modelMapper.map(updatedUserAddress, UserAddressRes.class);
    }

    @Override
    public void deleteUserAddress(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "UserAddress not found for this id :: " + id));
        userAddressRepository.delete(userAddress);
    }
}
