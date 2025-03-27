package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.repository.AvailableServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.AvailableServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void addAvailableService(WoodworkerProfile ww)
    {
        if (ww.getServicePack().getName().equals("Bronze"))
        {
            AvailableService availableService = new AvailableService();

            availableService.setService(serviceRepository.findAll().get(0));
            availableService.setWoodworkerProfile(ww);

            availableServiceRepository.save(availableService);
        }
        else if (ww.getServicePack().getName().equals("Sliver"))
        {
            for (int t = 0; t <= 1; t++)
            {
                AvailableService availableService = new AvailableService();
                availableService.setService(serviceRepository.findAll().get(t));
                availableService.setWoodworkerProfile(ww);

                availableServiceRepository.save(availableService);
            }
        }
        else
        {
            for (int t = 0; t <= 2; t++)
            {
                AvailableService availableService = new AvailableService();
                availableService.setService(serviceRepository.findAll().get(t));
                availableService.setWoodworkerProfile(ww);

                availableServiceRepository.save(availableService);
            }
        }
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
