package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.repository.ServicePackRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ServicePackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackServiceImpl implements ServicePackService {
    @Autowired
    private ServicePackRepository servicePackRepository;

    public ServicePackServiceImpl(ServicePackRepository servicePackRepository)
    {
        this.servicePackRepository = servicePackRepository;
    }

    public List<ServicePack> getAllServicePack()
    {
        return servicePackRepository.findAll();
    }
}
