package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceOrderService {
    List<ServiceOrder> listServiceOrderByUserIdOrWwId(Long id, String role);

    ServiceOrder addServiceOrderCustomize(CreateServiceOrderRequest createServiceOrderRequest);

    ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting);

    ServiceOrder customerFeedback(Long serviceOrderId, String feedback);

    ServiceOrder createServiceOrderPersonalize(CreateServiceOrderPersonalizeRequest createServiceOrderPersonalizeRequest);
}
