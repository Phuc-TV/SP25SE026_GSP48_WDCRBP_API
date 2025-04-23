package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServicePackConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Wallet;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<WoodworkerProfileListItemRes> getAllWoodWorker() {
        List<WoodworkerProfile> list = wwRepository.findAll();
        List<WoodworkerProfile> woodworkerProfileList = new ArrayList<>();

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals(ServicePackConstant.GOLD)) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals(ServicePackConstant.SILVER)) {
                    woodworkerProfileList.add(profile);
                }
            }
        }

        for (WoodworkerProfile profile : list) {
            if (profile != null) {
                if (profile.getServicePack() != null && profile.getServicePack().getName().equals(ServicePackConstant.BRONZE)) {
                    woodworkerProfileList.add(profile);
                }
            }
        }
        return woodworkerProfileList.stream().map(item -> modelMapper.map(item, WoodworkerProfileListItemRes.class)).toList();
    }

    @Override
    public WoodworkerProfileDetailRes getWoodworkerById(Long id)
    {
        WoodworkerProfile ww = wwRepository.findById(id).orElse(null);

        return modelMapper.map(ww, WoodworkerProfileDetailRes.class);
    }

    @Override
    public WoodworkerProfileDetailRes getWoodworkerByUserId(Long userId) {
        WoodworkerProfile wwProfile = wwRepository.findByUser_UserId(userId).orElse(null);

        return modelMapper.map(wwProfile, WoodworkerProfileDetailRes.class);
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
            woodworkerProfile.setStatus(false);
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
    public WoodworkerProfileListItemRes addServicePack(Long servicePackId, Long wwId) {
        WoodworkerProfile woodworker = wwRepository.findWoodworkerProfileByWoodworkerId(wwId);
        ServicePack newPack = servicePackRepository.findServicePackByServicePackId(servicePackId);

        LocalDateTime now = LocalDateTime.now();
        ServicePack currentPack = woodworker.getServicePack();
        LocalDateTime currentEndDate = woodworker.getServicePackEndDate();

        long convertedDays = getConvertedDays(currentPack, newPack, currentEndDate);

        if (currentPack != null &&
                currentEndDate != null &&
                currentEndDate.isAfter(now) &&
                currentPack.getName().equals(newPack.getName())) {

            woodworker.setServicePackEndDate(currentEndDate.plus(newPack.getDuration(), ChronoUnit.MONTHS));

        } else {
            woodworker.setServicePack(newPack);
            woodworker.setServicePackStartDate(now);
            woodworker.setServicePackEndDate(now.plusDays(convertedDays).plusMonths(newPack.getDuration()));
        }

        wwRepository.save(woodworker);
        availableServiceService.activateAvailableServicesByServicePack(woodworker, newPack.getName());

        return modelMapper.map(woodworker, WoodworkerProfileListItemRes.class);
    }

    private long getConvertedDays(ServicePack currentPack, ServicePack newPack, LocalDateTime currentEndDate) {
        Map<String, Double> packWeights = Map.of(
                "Bronze", 1.0,
                "Silver", 1.75,
                "Gold", 2.5
        );

        if (currentPack == null || newPack == null || currentEndDate == null || currentEndDate.isBefore(LocalDateTime.now())) {
            return 0;
        }

        double currentWeight = packWeights.getOrDefault(currentPack.getName(), 0.0);
        double newWeight = packWeights.getOrDefault(newPack.getName(), 0.0);

        if (newWeight < currentWeight) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không thể mua gói thấp hơn gói hiện tại.");
        }

        if (newWeight > currentWeight) {
            long remainingDays = ChronoUnit.DAYS.between(LocalDateTime.now(), currentEndDate);
            return Math.round((remainingDays * currentWeight) / newWeight);
        }

        return 0;
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
            throw new RuntimeException("Không thể cập nhật Service Pack vì xưởng mộc chưa được kích hoạt.");
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

    @Override
    public UpdateStatusPublicRes updatePublicStatus(UpdateStatusPublicRequest request) {
        WoodworkerProfile profile = wwRepository.findByUser_UserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xưởng mộc theo userId: " + request.getUserId()));

        profile.setPublicStatus(request.isPublicStatus());
        profile.setUpdatedAt(LocalDateTime.now());

        wwRepository.save(profile);

        return UpdateStatusPublicRes.builder()
                .woodworkerId(profile.getWoodworkerId())
                .updatedPublicStatus(request.isPublicStatus())
                .message("Cập nhật trạng thái công khai thành công")
                .build();
    }

    @Override
    public WoodworkerProfileDetailRes updateWoodworkerProfile(UpdateWoodworkerProfileRequest request) {
        WoodworkerProfile profile = wwRepository.findById(request.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xưởng mộc"));

        profile.setBrandName(request.getBrandName());
        profile.setBio(request.getBio());
        profile.setBusinessType(request.getBusinessType());
        profile.setImgUrl(request.getImgUrl());
        profile.setAddress(request.getAddress());
        profile.setWardCode(request.getWardCode());
        profile.setDistrictId(request.getDistrictId());
        profile.setCityId(request.getCityId());
        profile.setUpdatedAt(LocalDateTime.now());

        WoodworkerProfile updatedProfile = wwRepository.save(profile);
        return toDetailDto(updatedProfile);
    }


    private WoodworkerProfileDetailRes toDetailDto(WoodworkerProfile profile) {
        if (profile == null) return null;

        WoodworkerProfileDetailRes dto = new WoodworkerProfileDetailRes();
        dto.setWoodworkerId(profile.getWoodworkerId());
        dto.setBrandName(profile.getBrandName());
        dto.setBio(profile.getBio());
        dto.setWarrantyPolicy(profile.getWarrantyPolicy());
        dto.setBusinessType(profile.getBusinessType());
        dto.setTaxCode(profile.getTaxCode());
        dto.setImgUrl(profile.getImgUrl());
        dto.setAddress(profile.getAddress());
        dto.setWardCode(profile.getWardCode());
        dto.setDistrictId(profile.getDistrictId());
        dto.setCityId(profile.getCityId());
        dto.setTotalStar(profile.getTotalStar());
        dto.setTotalReviews(profile.getTotalReviews());
        dto.setServicePackStartDate(profile.getServicePackStartDate());
        dto.setServicePackEndDate(profile.getServicePackEndDate());
        dto.setPublicStatus(profile.getPublicStatus());
        dto.setServicePack(profile.getServicePack());

        if (profile.getUser() != null) {
            UserDetailRes userDto = new UserDetailRes();
            userDto.setUserId(profile.getUser().getUserId());
            userDto.setUsername(profile.getUser().getUsername());
            userDto.setEmail(profile.getUser().getEmail());
            userDto.setPhone(profile.getUser().getPhone());
            dto.setUser(userDto);
        }

        return dto;
    }

    @Override
    public WoodworkerProfileDetailRes updateWarrantyPolicyByWwId(UpdateWarrantyPolicyByWwIdRequest request) {
        WoodworkerProfile profile = wwRepository.findById(request.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xưởng mộc"));

        profile.setWarrantyPolicy(request.getWarrantyPolicy());
        profile.setUpdatedAt(LocalDateTime.now());
        WoodworkerProfile updated = wwRepository.save(profile);

        return toDetailDto(updated);
    }

}



