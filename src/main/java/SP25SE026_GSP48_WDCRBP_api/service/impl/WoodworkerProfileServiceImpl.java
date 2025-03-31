package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Wallet;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListRegisterRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateWoodworkerServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerUpdateStatusRes;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WalletRepository;
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

    @Autowired
    private WalletRepository walletRepository;

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
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals("Gold")) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals("Sliver")) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals("Bronze")) {
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
    public WoodworkerProfile getWoodworkerByUserId(Long userId) {
        return wwRepository.findByUser_UserId(userId).orElse(null);
    }


    @Override
    public WoodworkerProfileRes registerWoodworker(WoodworkerRequest request) {
        try {
            Optional<User> existingUser = userRepository.findUserByEmailOrPhone(request.getEmail(), request.getPhone());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email or Phone is already registered. Please use a different email or phone.");
            }
            ModelMapper customMapper = new ModelMapper();
            customMapper.addMappings(new PropertyMap<WoodworkerRequest, WoodworkerProfile>() {
                @Override
                protected void configure() {
                    // Skip the woodworkerId field because it's auto-generated
                    skip(destination.getWoodworkerId());
                }
            });
            WoodworkerProfile woodworkerProfile = customMapper.map(request, WoodworkerProfile.class);
            woodworkerProfile.setStatus(false);  // Default status
            woodworkerProfile.setCreatedAt(LocalDateTime.now());
            woodworkerProfile.setUpdatedAt(LocalDateTime.now());
            WoodworkerProfile savedProfile = wwRepository.save(woodworkerProfile);
            User user = createNewUser(request);
            savedProfile.setUser(user);
            WoodworkerProfile finalProfile = wwRepository.save(savedProfile);
            WoodworkerProfileRes response = new WoodworkerProfileRes();
            WoodworkerProfileRes.Data data = modelMapper.map(finalProfile, WoodworkerProfileRes.Data.class);
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
            TempPasswordStorage.storePlainPassword(savedUser.getUserId(), randomPassword);

            return savedUser;

        } catch (Exception e) {
            throw new RuntimeException("Error during user creation: " + e.getMessage());
        }
    }

    @Override
    public WoodworkerUpdateStatusRes updateWoodworkerStatus(WoodworkerUpdateStatusRequest request) {
        Long woodworkerId = Long.parseLong(request.getWoodworkerId());
        Optional<WoodworkerProfile> woodworkerProfileOptional = wwRepository.findById(woodworkerId);

        if (woodworkerProfileOptional.isEmpty()) {
            throw new RuntimeException("Woodworker not found");
        }

        WoodworkerProfile woodworkerProfile = woodworkerProfileOptional.get();
        User user = woodworkerProfile.getUser();

        if (user == null) {
            throw new RuntimeException("User associated with this woodworker not found");
        }

        boolean requestedStatus = request.isStatus();
        String reason = request.getDescription();

        if (!requestedStatus && reason != null && !reason.trim().isEmpty()) {
            mailServiceImpl.sendEmail(user.getEmail(),
                    "Yêu cầu cập nhật trạng thái bị từ chối",
                    "status-rejection",
                    reason);

            wwRepository.delete(woodworkerProfile);
            userRepository.delete(user);
            WoodworkerUpdateStatusRes response = new WoodworkerUpdateStatusRes();
            response.setUpdatedAt(LocalDateTime.now());
            return response;
        }

        woodworkerProfile.setStatus(true);
        woodworkerProfile.setUpdatedAt(LocalDateTime.now());
        wwRepository.save(woodworkerProfile);

        String plainPassword = TempPasswordStorage.getPlainPassword(user.getUserId());
        if (plainPassword != null) {
            sendPasswordToUser(user.getEmail(), plainPassword);
        }

        if (walletRepository.findByUser_UserId(user.getUserId()).isEmpty()) {
            Wallet wallet = Wallet.builder()
                    .user(user)
                    .balance(0f)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            walletRepository.save(wallet);
        }

        // Set available services for the woodworker for false for all service
        availableServiceService.addAvailableServiceByServiceName(woodworkerProfile, ServiceNameConstant.CUSTOMIZATION);
        availableServiceService.addAvailableServiceByServiceName(woodworkerProfile, ServiceNameConstant.GUARANTEE);
        availableServiceService.addAvailableServiceByServiceName(woodworkerProfile, ServiceNameConstant.SALE);
        availableServiceService.addAvailableServiceByServiceName(woodworkerProfile, ServiceNameConstant.PERSONALIZATION);

        WoodworkerUpdateStatusRes response = new WoodworkerUpdateStatusRes();
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
        obj.setServicePackEndDate(LocalDateTime.now().plus(servicePack.getDuration(), ChronoUnit.MONTHS));

        wwRepository.save(obj);

        availableServiceService.activateAvailableServicesByServicePack(obj,servicePack.getName());

        return obj;

    }

    @Override
    public List<ListRegisterRes.Data> getAllInactiveWoodworkers() {
        List<WoodworkerProfile> inactiveProfiles = wwRepository.findByStatusFalse();

        return inactiveProfiles.stream().map(profile -> {
            ListRegisterRes.Data dto = new ListRegisterRes.Data();
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

    @Override
    public UpdateWoodworkerServicePackRest updateServicePackForWoodworker(UpdateWoodworkerServicePackRequest request) {
        WoodworkerProfile profile = wwRepository.findById(request.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("Woodworker not found"));

        if (!profile.isStatus()) {
            throw new RuntimeException("Không thể cập nhật Service Pack vì thợ mộc chưa được kích hoạt.");
        }

        ServicePack servicePack = servicePackRepository.findById(request.getServicePackId())
                .orElseThrow(() -> new RuntimeException("Service Pack not found"));

        profile.setServicePack(servicePack);
        profile.setServicePackStartDate(LocalDateTime.now());
        profile.setServicePackEndDate(LocalDateTime.now().plusMonths(servicePack.getDuration()));
        profile.setUpdatedAt(LocalDateTime.now());

        wwRepository.save(profile);

        UpdateWoodworkerServicePackRest.Data data = new UpdateWoodworkerServicePackRest.Data();
        data.setWoodworkerId(profile.getWoodworkerId());
        data.setServicePackId(servicePack.getServicePackId());
        data.setServicePackStartDate(profile.getServicePackStartDate());
        data.setServicePackEndDate(profile.getServicePackEndDate());
        data.setBrandName(profile.getBrandName());
        data.setBio(profile.getBio());
        data.setImgUrl(profile.getImgUrl());
        data.setBusinessType(profile.getBusinessType());
        data.setTaxCode(profile.getTaxCode());
        data.setAddress(profile.getAddress());
        data.setWardCode(profile.getWardCode());
        data.setDistrictId(profile.getDistrictId());
        data.setCityId(profile.getCityId());
        data.setCreatedAt(profile.getCreatedAt());
        data.setUpdatedAt(profile.getUpdatedAt());

        UpdateWoodworkerServicePackRest response = new UpdateWoodworkerServicePackRest();
        response.setData(data);
        return response;
    }

}

