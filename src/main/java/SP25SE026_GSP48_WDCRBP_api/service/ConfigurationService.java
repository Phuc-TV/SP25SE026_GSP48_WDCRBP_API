package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationSearchRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ConfigurationUpsertRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ConfigurationRes;

import java.util.List;

public interface ConfigurationService {
    String getValue(String name);
    ConfigurationRes update(ConfigurationUpdateRequest request);
    CoreApiResponse getAllConfiguration();
    List<ConfigurationRes> getAll();
    List<ConfigurationRes> getByDescription(ConfigurationSearchRequest request);
    ConfigurationRes update(ConfigurationUpsertRequest request);
    void delete(Long id);
}
