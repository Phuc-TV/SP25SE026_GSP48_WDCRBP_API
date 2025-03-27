package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WoodworkerProfileServiceImpl implements WoodworkerProfileService {
    @Autowired
    private WoodworkerProfileRepository wwRepository;

    @Autowired
    private ServicePackRepository servicePackRepository;

    @Autowired
    private AvailableServiceService availableServiceService;

    public WoodworkerProfileServiceImpl(WoodworkerProfileRepository repository,
                                        ServicePackRepository servicePackRepository,
                                        AvailableServiceService availableServiceService) {
        this.wwRepository = repository;
        this.servicePackRepository = servicePackRepository;
        this.availableServiceService = availableServiceService;
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

