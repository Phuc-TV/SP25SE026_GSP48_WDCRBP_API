package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Contract;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WwCreateContractCustomizeRequest;
import SP25SE026_GSP48_WDCRBP_api.service.ContractService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/Contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    public ContractController (ContractService contractService)
    {
        this.contractService = contractService;
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/createContractCustomize")
    public CoreApiResponse createContractCustomize(@RequestBody WwCreateContractCustomizeRequest request)
    {
        Contract contract = contractService.createContractCustomize(request);

        return CoreApiResponse.success(contract);
    }
}
