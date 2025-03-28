package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerUpdateStatusRest;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;


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
    private ServicePackRepository servicePackRepository;

    @Autowired
    private AvailableServiceService availableServiceService;

    @Autowired
    private AuthServiceImpl authServiceImpl;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    public WoodworkerProfileServiceImpl(WoodworkerProfileRepository repository,
                                        ServicePackRepository servicePackRepository,
                                        AvailableServiceService availableServiceService,
                                        ModelMapper modelMapper) {
        this.wwRepository = repository;
        this.servicePackRepository = servicePackRepository;
        this.availableServiceService = availableServiceService;
        this.modelMapper = modelMapper;
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
    public WoodworkerProfileRest registerWoodworker(WoodworkerRequest request) {
        // Check if email or phone already exists
        Optional<User> existingUser = userRepository.findUserByEmailOrPhone(request.getEmail(), request.getPhone());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email or Phone is already registered. Please use a different email or phone.");
        }

        // Create the woodworker profile
        WoodworkerProfile woodworkerProfile = modelMapper.map(request, WoodworkerProfile.class);
        woodworkerProfile.setCreatedAt(LocalDateTime.now());
        woodworkerProfile.setUpdatedAt(LocalDateTime.now());

        // Save the woodworker profile
        WoodworkerProfile savedProfile = wwRepository.save(woodworkerProfile);

        // Create the user and assign userId to the woodworker profile
        User newUser = createNewUser(request);

        // Set the userId in the woodworker profile
        savedProfile.setUser(newUser);
        wwRepository.save(savedProfile);  // Save the updated woodworker profile with userId set

        // Prepare the response
        WoodworkerProfileRest response = new WoodworkerProfileRest();
        response.setStatus("Success");
        response.setMessage("Woodworker registered successfully.");

        WoodworkerProfileRest.Data data = new WoodworkerProfileRest.Data();
        data.setWoodworkerId(savedProfile.getWoodworkerId());
        data.setBrandName(savedProfile.getBrandName());
        data.setBio(savedProfile.getBio());
        data.setImgUrl(savedProfile.getImgUrl());
        data.setBusinessType(savedProfile.getBusinessType());
        data.setTaxCode(savedProfile.getTaxCode());
        data.setCreatedAt(savedProfile.getCreatedAt());
        data.setUpdatedAt(savedProfile.getUpdatedAt());

        response.setData(data);
        return response;
    }

    private User createNewUser(WoodworkerRequest request) {
        try {
            // Generate a random password for the new user
            String randomPassword = RandomStringUtils.randomAlphabetic(8);  // Random password of length 8

            // Create a new user object
            User user = new User();
            user.setUsername(request.getFullName());  // Use fullName as the username
            user.setPassword(randomPassword);  // Set the random password
            user.setEmail(request.getEmail());  // Use the email from the request
            user.setPhone(request.getPhone());  // Use the phone from the request
            user.setRole("Woodworker");  // Set role as Woodworker
            user.setStatus(true);  // Assuming we activate the user immediately
            user.setCreatedAt(LocalDateTime.now());

            // Save the user directly using UserRepository
            return userRepository.save(user);  // Return the saved user object

        } catch (Exception e) {
            throw new RuntimeException("Error during user creation after woodworker registration: " + e.getMessage());
        }
    }

    @Override
    public WoodworkerUpdateStatusRest updateWoodworkerStatus(WoodworkerUpdateStatusRequest request) {
        // Find the user by ID
        Optional<User> userOptional = userRepository.findById(Long.parseLong(request.getUserId()));
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Get the user object
        User user = userOptional.get();

        // Find the woodworker by ID
        Optional<WoodworkerProfile> woodworkerProfileOptional = wwRepository.findById(Long.parseLong(request.getWoodworkerId()));
        if (woodworkerProfileOptional.isEmpty()) {
            throw new RuntimeException("Woodworker not found");
        }

        // Get the woodworker profile
        WoodworkerProfile woodworkerProfile = woodworkerProfileOptional.get();

        // Update the status
        woodworkerProfile.setStatus(request.isStatus());
        woodworkerProfile.setUpdatedAt(LocalDateTime.now());  // Set the updated timestamp

        // Save the updated profile
        wwRepository.save(woodworkerProfile);

        // Create the response object
        WoodworkerUpdateStatusRest response = new WoodworkerUpdateStatusRest();
        response.setStatus("Success");
        response.setMessage("Woodworker status updated successfully.");
        response.setUpdatedAt(woodworkerProfile.getUpdatedAt());
        sendPasswordToUser(user.getEmail(), user.getPassword());
        return response;
    }

    private void sendPasswordToUser(String email, String password) {
        try {
            // Send the email with the password to the user
            mailService.sendEmail(email, "Your Woodworker Account Password", "password", password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password email: " + e.getMessage());
        }
    }

    @Override
    public WoodworkerProfile addServicePack(Long servicePackId, Long wwId)
    {
        WoodworkerProfile obj = wwRepository.findWoodworkerProfileByWoodworkerId(wwId);

        ServicePack servicePack = servicePackRepository.findServicePackByServicePackId(servicePackId);

        obj.setServicePack(servicePack);
        obj.setServicePackStartDate(LocalDateTime.now());

        // Cộng Duration vào LocalDateTime
        obj.setServicePackEndDate(LocalDateTime.now().plus(servicePack.getDuration(), ChronoUnit.MONTHS));

        wwRepository.save(obj);

        availableServiceService.addAvailableService(obj);

        return obj;
    }
}

