package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WoodworkerRequest;

import java.util.List;

public interface WoodworkerProfileService {
    List<WoodworkerProfile> getAllWoodWorker();

    WoodworkerProfile getWoodworkerById(Long id);

    WoodworkerProfile registerWoodworker(WoodworkerRequest request);
}
