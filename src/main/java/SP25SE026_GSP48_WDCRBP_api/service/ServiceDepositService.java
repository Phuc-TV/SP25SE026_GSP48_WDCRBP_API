package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceWithDepositsRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateServiceDepositPercentRes;

import java.util.List;

public interface ServiceDepositService {
    List<UpdateServiceDepositPercentRes> updatePercentAllServiceDepositByServiceId(Long serviceId, Short newPercent);
    List<ServiceWithDepositsRes> getAllServiceWithDeposits();
}
