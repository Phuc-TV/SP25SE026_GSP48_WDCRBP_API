package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;

import java.util.List;

public interface OrderDepositService {
    List<OrderDeposit> getAllOrderDepositByOrderId(Long orderId);
}
