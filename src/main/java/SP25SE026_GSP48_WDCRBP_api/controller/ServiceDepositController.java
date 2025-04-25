package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateServiceDepositPercentRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceWithDepositsRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateServiceDepositPercentRes;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceDepositService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/service-deposits")
@RequiredArgsConstructor
public class ServiceDepositController {

    private final ServiceDepositService serviceDepositService;

    @PutMapping("/update-percent")
    public CoreApiResponse<List<UpdateServiceDepositPercentRes>> updatePercentForAllDeposits(
            @RequestBody UpdateServiceDepositPercentRequest request) {
        try {
            List<UpdateServiceDepositPercentRes> updatedDeposits =serviceDepositService.updateDepositPercents(request);
            return CoreApiResponse.success(updatedDeposits, "Updated all deposits successfully");
        } catch (EntityNotFoundException ex) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    @GetMapping("/all")
    public CoreApiResponse<List<ServiceWithDepositsRes>> getAllServiceWithDeposits() {
        try {
            List<ServiceWithDepositsRes> response = serviceDepositService.getAllServiceWithDeposits();
            return CoreApiResponse.success(response, "Fetched all service deposits successfully");
        } catch (Exception ex) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to fetch data");
        }
    }
}
