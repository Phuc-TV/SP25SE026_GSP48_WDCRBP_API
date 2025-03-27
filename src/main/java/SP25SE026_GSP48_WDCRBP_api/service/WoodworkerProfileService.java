package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkerProfileDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;

import java.util.List;

public interface WoodworkerProfileService {
    List<WoodworkerProfile> getAllWoodWorker();

    WoodworkerProfile getWoodworkerById(Long id);

    WoodworkerProfile addServicePack(Long servicePackId, Long wwId);
}
