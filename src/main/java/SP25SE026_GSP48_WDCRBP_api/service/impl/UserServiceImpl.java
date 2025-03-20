package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserInfoResponse;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}
