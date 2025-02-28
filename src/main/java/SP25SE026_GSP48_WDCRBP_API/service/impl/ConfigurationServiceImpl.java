package SP25SE026_GSP48_WDCRBP_API.service.impl;

import SP25SE026_GSP48_WDCRBP_API.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_API.components.CoreResponse;
import SP25SE026_GSP48_WDCRBP_API.model.entity.Configuration;
import SP25SE026_GSP48_WDCRBP_API.repository.ConfigurationRepository;
import SP25SE026_GSP48_WDCRBP_API.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    public void setConfigurationRepository(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public CoreApiResponse getAllConfiguration() {
        return CoreApiResponse.success(configurationRepository.findAll(), "Successfully");
    }
}
