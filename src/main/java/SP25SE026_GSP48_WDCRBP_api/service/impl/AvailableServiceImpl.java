package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServicePackConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.repository.AvailableServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailableServiceImpl implements AvailableServiceService {
    @Autowired
    private AvailableServiceRepository availableServiceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private WoodworkerProfileRepository woodworkerProfileRepository;

    public AvailableServiceImpl(AvailableServiceRepository availableServiceRepository)
    {
        this.availableServiceRepository = availableServiceRepository;
    }

    @Override
    public void activateAvailableServicesByServicePack(WoodworkerProfile ww, String servicePackName)
    {
        List<AvailableService> availableServiceList = availableServiceRepository.findAvailableServicesByWoodworkerProfile(ww);

        switch (servicePackName) {
            case ServicePackConstant.GOLD:
                for (AvailableService availableService : availableServiceList) {
                    availableService.setStatus(true);
                    availableServiceRepository.save(availableService);
                }
                break;
            case ServicePackConstant.SILVER:
                for (AvailableService availableService : availableServiceList) {
                    if (availableService.getService().getServiceName().equals(ServiceNameConstant.PERSONALIZATION)) {
                        availableService.setStatus(false);
                        availableServiceRepository.save(availableService);
                    } else {
                        availableService.setStatus(true);
                        availableServiceRepository.save(availableService);
                    }
                }
                break;
            case ServicePackConstant.BRONZE:
                for (AvailableService availableService : availableServiceList) {
                    if (availableService.getService().getServiceName().equals(ServiceNameConstant.PERSONALIZATION)
                            || availableService.getService().getServiceName().equals(ServiceNameConstant.SALE)) {
                        availableService.setStatus(false);
                        availableServiceRepository.save(availableService);
                    } else {
                        availableService.setStatus(true);
                        availableServiceRepository.save(availableService);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void addAvailableServiceByServiceName(WoodworkerProfile ww, String serviceName) {
        AvailableService availableService = new AvailableService();
        availableService.setService(serviceRepository.findFirstByServiceName(serviceName));
        availableService.setStatus(false);
        availableService.setOperatingStatus(true);
        availableService.setCreatedAt(LocalDateTime.now());
        availableService.setUpdatedAt(LocalDateTime.now());
        availableService.setWoodworkerProfile(ww);

        availableServiceRepository.save(availableService);
    }

    @Override
    public List<AvailableService> getAvailableServiceByWwId(Long wwId)
    {
        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(wwId);

        List<AvailableService> availableService =
                availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworkerProfile);

        return availableService;
    }
}
