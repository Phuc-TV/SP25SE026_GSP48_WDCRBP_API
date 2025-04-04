package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateStatusPublicRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;

import java.util.List;

public interface WoodworkerProfileService {
    List<WoodworkerProfile> getAllWoodWorker();

    WoodworkerProfile getWoodworkerById(Long id);

    WoodworkerProfile getWoodworkerByUserId(Long userId);

    WoodworkerProfileRes registerWoodworker(WoodworkerRequest request);

    WoodworkerUpdateStatusRes updateWoodworkerStatus(WoodworkerUpdateStatusRequest request);

    WoodworkerProfile addServicePack(Long servicePackId, Long wwId);

    List<ListRegisterRes.Data> getAllInactiveWoodworkers();

    UpdateWoodworkerServicePackRest updateServicePackForWoodworker(UpdateWoodworkerServicePackRequest request);

    UpdateStatusPublicRes updatePublicStatus(UpdateStatusPublicRequest request);

}
