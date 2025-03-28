package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListRegisterRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerUpdateStatusRest;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import SP25SE026_GSP48_WDCRBP_api.util.PasswordHashUtil;
import SP25SE026_GSP48_WDCRBP_api.util.TempPasswordStorage;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
    private MailServiceImpl mailServiceImpl;

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
        try {
            // Step 1: Check if email or phone already exists in the User table
            Optional<User> existingUser = userRepository.findUserByEmailOrPhone(request.getEmail(), request.getPhone());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email or Phone is already registered. Please use a different email or phone.");
            }

            // Step 2: Configure ModelMapper to skip the auto-generated woodworkerId field
            ModelMapper customMapper = new ModelMapper();
            customMapper.addMappings(new PropertyMap<WoodworkerRequest, WoodworkerProfile>() {
                @Override
                protected void configure() {
                    // Skip the woodworkerId field because it's auto-generated
                    skip(destination.getWoodworkerId());
                }
            });

            // Map the request to the WoodworkerProfile entity
            WoodworkerProfile woodworkerProfile = customMapper.map(request, WoodworkerProfile.class);
            woodworkerProfile.setStatus(false);  // Default status
            woodworkerProfile.setCreatedAt(LocalDateTime.now());
            woodworkerProfile.setUpdatedAt(LocalDateTime.now());

            // Step 3: Save the WoodworkerProfile without the user for now
            WoodworkerProfile savedProfile = wwRepository.save(woodworkerProfile);

            // Step 4: Create the new User
            User user = createNewUser(request);

            // Step 5: Associate the User with the WoodworkerProfile
            savedProfile.setUser(user);

            // Step 6: Save the updated WoodworkerProfile with the User
            WoodworkerProfile finalProfile = wwRepository.save(savedProfile);

            // Step 7: Map the saved profile to the response DTO (WoodworkerProfileRest)
            WoodworkerProfileRest response = new WoodworkerProfileRest();

            // Use ModelMapper to map from entity to response DTO
            WoodworkerProfileRest.Data data = modelMapper.map(finalProfile, WoodworkerProfileRest.Data.class);
            response.setData(data);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error during woodworker registration: " + e.getMessage());
        }
    }

    private User createNewUser(WoodworkerRequest request) {
        try {
            String randomPassword = RandomStringUtils.randomAlphabetic(8);  // Generate random password
            String hashedPassword = PasswordHashUtil.hashPassword(randomPassword);

            User user = new User();
            user.setUsername(request.getFullName());
            user.setPassword(hashedPassword);
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setRole("Woodworker");
            user.setStatus(true);
            user.setCreatedAt(LocalDateTime.now());

            User savedUser = userRepository.save(user);

            // ✅ Store the plain password temporarily in memory
            TempPasswordStorage.storePlainPassword(savedUser.getUserId(), randomPassword);

            return savedUser;

        } catch (Exception e) {
            throw new RuntimeException("Error during user creation: " + e.getMessage());
        }
    }


    @Override
    public WoodworkerUpdateStatusRest updateWoodworkerStatus(WoodworkerUpdateStatusRequest request) {
        // Step 1: Validate woodworker ID exists
        Long woodworkerId = Long.parseLong(request.getWoodworkerId());
        Optional<WoodworkerProfile> woodworkerProfileOptional = wwRepository.findById(woodworkerId);
        if (woodworkerProfileOptional.isEmpty()) {
            throw new RuntimeException("Woodworker not found");
        }

        // Step 2: Get woodworker profile and related user
        WoodworkerProfile woodworkerProfile = woodworkerProfileOptional.get();
        User user = woodworkerProfile.getUser(); // thanks to @OneToOne relation

        if (user == null) {
            throw new RuntimeException("User associated with this woodworker not found");
        }

        // Step 3: Update status
        woodworkerProfile.setStatus(request.isStatus());
        woodworkerProfile.setUpdatedAt(LocalDateTime.now());
        wwRepository.save(woodworkerProfile);

        // Step 4: Get the plain password from TempPasswordStorage
        String plainPassword = TempPasswordStorage.getPlainPassword(user.getUserId());

        // Step 5: Send password to email (only if available)
        if (plainPassword != null) {
            sendPasswordToUser(user.getEmail(), plainPassword);
        }

        // Step 6: Return response
        WoodworkerUpdateStatusRest response = new WoodworkerUpdateStatusRest();
        response.setUpdatedAt(woodworkerProfile.getUpdatedAt());
        return response;
    }

    private void sendPasswordToUser(String email, String password) {
        try {
            // Send the email with the password to the user
            mailServiceImpl.sendEmail(email, "Your Woodworker Account Password", "password", password);
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

    @Override
    public List<ListRegisterRest.Data> getAllInactiveWoodworkers() {
        List<WoodworkerProfile> inactiveProfiles = wwRepository.findByStatusFalse();

        return inactiveProfiles.stream().map(profile -> {
            ListRegisterRest.Data dto = new ListRegisterRest.Data();
            dto.setWoodworkerId(profile.getWoodworkerId());
            dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
            dto.setFullName(profile.getUser() != null ? profile.getUser().getUsername() : null);
            dto.setEmail(profile.getUser() != null ? profile.getUser().getEmail() : null);
            dto.setPhone(profile.getUser() != null ? profile.getUser().getPhone() : null);
            dto.setBrandName(profile.getBrandName());
            dto.setBio(profile.getBio());
            dto.setImgUrl(profile.getImgUrl());
            dto.setBusinessType(profile.getBusinessType());
            dto.setTaxCode(profile.getTaxCode());
            dto.setStatus(String.valueOf(profile.isStatus()));
            dto.setAddress(profile.getAddress());
            dto.setWardCode(profile.getWardCode());
            dto.setDistrictId(profile.getDistrictId());
            dto.setCityId(profile.getCityId());
            dto.setCreatedAt(profile.getCreatedAt());
            dto.setUpdatedAt(profile.getUpdatedAt());
            return dto;
        }).toList();
    }
}

