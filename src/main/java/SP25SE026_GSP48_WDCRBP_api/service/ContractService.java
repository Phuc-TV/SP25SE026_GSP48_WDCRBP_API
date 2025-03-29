package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Contract;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ContractCustomizeRequest;

public interface ContractService {
    Contract createContractCustomize(ContractCustomizeRequest contractCustomizeRequest);

    Contract customerSignContract(Long serviceOrderId, String customerSign, Long cusId);
}
