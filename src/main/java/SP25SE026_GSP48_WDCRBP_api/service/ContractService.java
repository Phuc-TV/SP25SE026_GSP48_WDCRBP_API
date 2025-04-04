package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Contract;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WwCreateContractCustomizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ContractDetailRes;

public interface ContractService {
    Contract createContractCustomize(WwCreateContractCustomizeRequest wwCreateContractCustomizeRequest);

    Contract customerSignContract(Long serviceOrderId, String customerSign, Long cusId);

    ContractDetailRes getContractByserviceorderId(Long serviceOrderId);
}
