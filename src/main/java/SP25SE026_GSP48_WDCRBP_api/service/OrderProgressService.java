package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderProgress;

import java.util.List;

public interface OrderProgressService {
    List<OrderProgress> getAllOrderProgressByOrderId(Long orderId);
    List<OrderProgress> getAllOrderProgressByGuaranteeId(Long orderId);
}
