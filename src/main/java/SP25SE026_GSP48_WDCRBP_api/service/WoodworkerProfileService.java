package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListRegisterRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateWoodworkerServicePackRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerUpdateStatusRest;

import java.util.List;

public interface WoodworkerProfileService {
    List<WoodworkerProfile> getAllWoodWorker();

    WoodworkerProfile getWoodworkerById(Long id);

    WoodworkerProfileRest registerWoodworker(WoodworkerRequest request);

    WoodworkerUpdateStatusRest updateWoodworkerStatus(WoodworkerUpdateStatusRequest request);

    WoodworkerProfile addServicePack(Long servicePackId, Long wwId);

    List<ListRegisterRest.Data> getAllInactiveWoodworkers();

    UpdateWoodworkerServicePackRest updateServicePackForWoodworker(UpdateWoodworkerServicePackRequest request);
}
