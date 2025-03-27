package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;

import java.util.List;

public interface ServiceOrderService {
    List<ServiceOrder> listServiceOrderByUserIdOrWwId(Long id, String role);

    ServiceOrder addServiceOrderCustomize(Long availableServiceId, Long userId, Long designIdeaVariantId);

    ServiceOrder acceptServiceOrder(Long serviceOrderId);
}
