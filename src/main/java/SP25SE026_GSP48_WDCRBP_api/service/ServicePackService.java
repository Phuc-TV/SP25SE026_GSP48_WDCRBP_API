package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackRes;

import java.util.List;

public interface ServicePackService {
    CreateServicePackRes createServicePack(CreateServicePackRequest request);
    CreateServicePackRes updateServicePack(Long servicePackId, CreateServicePackRequest request);
    void deleteServicePack(Long servicePackId);
    List<ListServicePackRes.Data> getAllServicePacks();
    List<ListServicePackRes.Data> getAllActiveServicePacks();
    ListServicePackRes getServicePackById(Long servicePackId);

}
