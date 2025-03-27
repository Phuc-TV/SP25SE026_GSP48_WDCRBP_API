package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;

import java.util.List;

public interface AvailableServiceService {
    void addAvailableService(WoodworkerProfile ww);

    List<AvailableService> getAvailableServiceByWwId(Long wwId);
}
