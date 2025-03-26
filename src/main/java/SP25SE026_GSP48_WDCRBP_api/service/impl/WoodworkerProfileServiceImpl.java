package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WoodworkerProfileServiceImpl implements WoodworkerProfileService {
    @Autowired
    private WoodworkerProfileRepository wwRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public WoodworkerProfileServiceImpl(WoodworkerProfileRepository repository) {
        this.wwRepository = repository;
    }

    @Override
    public List<WoodworkerProfile> getAllWoodWorker() {
        List<WoodworkerProfile> list = wwRepository.findAll();
        List<WoodworkerProfile> woodworkerProfileList = new ArrayList<>();

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack().getName().equals("Gold")) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack().getName().equals("Sliver")) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack().getName().equals("Bronze")) {
                    woodworkerProfileList.add(profile);
                }
            }
        }
        return woodworkerProfileList;
    }

    @Override
    public WoodworkerProfile getWoodworkerById(Long id)
    {
        WoodworkerProfile obj = wwRepository.findById(id).orElse(null);

        return obj;
    }

    @Override
    public WoodworkerProfile registerWoodworker(WoodworkerRequest request) {
        // Check if the email or phone already exists in the User table
        Optional<User> existingUser = userRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User with the provided email or phone does not exist");
        }

        // Now that the user exists, we associate the userId
        User user = existingUser.get();

        // Convert the WoodworkerRequest DTO to WoodworkerProfile entity
        WoodworkerProfile woodworkerProfile = modelMapper.map(request, WoodworkerProfile.class);
        woodworkerProfile.setCreatedAt(LocalDateTime.now());
        woodworkerProfile.setUpdatedAt(LocalDateTime.now());
        woodworkerProfile.setUser(user); // Set the User associated with this WoodworkerProfile

        // Save the WoodworkerProfile
        return wwRepository.save(woodworkerProfile);
    }
}

