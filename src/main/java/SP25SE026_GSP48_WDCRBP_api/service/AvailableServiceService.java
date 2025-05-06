package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.AvailableServiceUpdateReq;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.AvailableServiceListItemRes;

import java.util.List;

public interface AvailableServiceService {
    void activateAvailableServicesByServicePack(WoodworkerProfile ww, Long servicePackId);

    void addAvailableServiceByServiceName(WoodworkerProfile ww, String serviceName);

    AvailableService updateAvailableService(AvailableServiceUpdateReq updateReq);

    List<AvailableService> getAvailableServiceByWwId(Long wwId);
}
