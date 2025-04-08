package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Configuration;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationSearchRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationUpsertRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ConfigurationRes;
import SP25SE026_GSP48_WDCRBP_api.repository.ConfigurationRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ConfigurationRes create(ConfigurationUpsertRequest request) {
        Configuration config = Configuration.builder()
                .description(request.getDescription())
                .value(request.getValue())
                .createdBy(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Configuration saved = configurationRepository.save(config);
        return toDTO(saved);
    }

    @Override
    public ConfigurationRes update(ConfigurationUpsertRequest request) {
        Configuration config = configurationRepository.findById(request.getConfigurationId())
                .orElseThrow(() -> new RuntimeException("Configuration not found"));

        config.setDescription(request.getDescription());
        config.setValue(request.getValue());
        config.setUpdatedAt(LocalDateTime.now());

        Configuration updated = configurationRepository.save(config);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!configurationRepository.existsById(id)) {
            throw new RuntimeException("Configuration not found");
        }
        configurationRepository.deleteById(id);
    }

    @Override
    public List<ConfigurationRes> getAll() {
        return configurationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConfigurationRes> getByDescription(ConfigurationSearchRequest request) {
        List<Configuration> configs = configurationRepository.findByDescriptionContainingIgnoreCaseOrValueContainingIgnoreCase(
                request.getDescription() != null ? request.getDescription() : "",
                request.getValue() != null ? request.getValue() : ""
        );

        return configs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ConfigurationRes toDTO(Configuration entity) {
        return ConfigurationRes.builder()
                .configurationId(entity.getConfigurationId())
                .description(entity.getDescription())
                .value(entity.getValue())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
