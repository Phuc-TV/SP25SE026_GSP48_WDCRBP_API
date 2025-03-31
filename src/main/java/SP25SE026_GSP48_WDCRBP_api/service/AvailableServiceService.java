package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;

import java.util.List;

public interface AvailableServiceService {
    void activateAvailableServicesByServicePack(WoodworkerProfile ww, String servicePackName);

    void addAvailableServiceByServiceName(WoodworkerProfile ww, String serviceName);

    List<AvailableService> getAvailableServiceByWwId(Long wwId);
}
