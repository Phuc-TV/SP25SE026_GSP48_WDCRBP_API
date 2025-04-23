package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GetWarrantyPolicyByWwIdRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWarrantyPolicyByWwIdRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerProfileRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateStatusPublicRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerUpdateStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;

import java.util.List;

public interface WoodworkerProfileService {
    List<WoodworkerProfileListItemRes> getAllWoodWorker();

    WoodworkerProfileDetailRes getWoodworkerById(Long id);

    WoodworkerProfileDetailRes getWoodworkerByUserId(Long userId);

    WoodworkerProfileRes registerWoodworker(WoodworkerRequest request);

    WoodworkerUpdateStatusRes updateWoodworkerStatus(WoodworkerUpdateStatusRequest request);

    WoodworkerProfileListItemRes addServicePack(Long servicePackId, Long wwId);

    List<ListRegisterRes.Data> getAllInactiveWoodworkers();

    UpdateWoodworkerServicePackRest updateServicePackForWoodworker(UpdateWoodworkerServicePackRequest request);

    UpdateStatusPublicRes updatePublicStatus(UpdateStatusPublicRequest request);

    WoodworkerProfileDetailRes updateWoodworkerProfile(UpdateWoodworkerProfileRequest request);

    WoodworkerProfileDetailRes updateWarrantyPolicyByWwId(UpdateWarrantyPolicyByWwIdRequest request);
}
