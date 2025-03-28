package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServicePackDetailsRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CreateServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DeleteServicePackDetailsRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListServicePackDetailsRest;

public interface ServicePackDetailsService {
    CreateServicePackDetailsRest createServicePackDetails(CreateServicePackDetailsRequest request);
    CreateServicePackDetailsRest updateServicePackDetails(Long servicePackDetailsId, CreateServicePackDetailsRequest request);
    DeleteServicePackDetailsRest deleteServicePackDetails(Long servicePackDetailsId);
    ListServicePackDetailsRest getAllServicePackDetails();
    ListServicePackDetailsRest getServicePackDetailsById(Long servicePackDetailsId);
}

