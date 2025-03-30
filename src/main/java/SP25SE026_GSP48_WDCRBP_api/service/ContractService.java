package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Contract;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WwCreateContractCustomizeRequest;

public interface ContractService {
    Contract createContractCustomize(WwCreateContractCustomizeRequest wwCreateContractCustomizeRequest);

    Contract customerSignContract(Long serviceOrderId, String customerSign, Long cusId);
}
