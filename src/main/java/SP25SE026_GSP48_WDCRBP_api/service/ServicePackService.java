package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRest;

import java.util.List;

public interface ServicePackService {
    CreateServicePackRest createServicePack(CreateServicePackRequest request);
    CreateServicePackRest updateServicePack(Long servicePackId, CreateServicePackRequest request);
    void deleteServicePack(Long servicePackId);
    List<ListServicePackRest.Data> getAllServicePacks();
    ListServicePackRest getServicePackById(Long servicePackId);

}
