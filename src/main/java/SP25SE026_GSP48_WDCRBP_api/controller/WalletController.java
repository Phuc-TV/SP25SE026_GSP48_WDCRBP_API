package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WalletRest;
import SP25SE026_GSP48_WDCRBP_api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class WalletController {

    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/user/{userId}")
    public CoreApiResponse<WalletRest> getWalletByUserId(@PathVariable Long userId) {
        try {
            WalletRest wallet = walletService.getWalletByUserId(userId);
            return CoreApiResponse.success(wallet, "Lấy thông tin ví thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy thông tin ví thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public CoreApiResponse<WalletRest> updateBalanceWallet(@RequestBody WalletRequest walletRequest) {
        try {
            WalletRest updatedWallet = walletService.updateBalanceWallet(walletRequest);
            return CoreApiResponse.success(updatedWallet, "Cập nhật số dư ví thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Cập nhật số dư ví thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/order/payment")
    public CoreApiResponse<ListTransactionRest> createWalletOrderPayment(@RequestBody PaymentOrderRequest request) {
        try {
            ListTransactionRest response = walletService.createWalletOrderPayment(request);
            return CoreApiResponse.success(response, "Order payment created successfully.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to create order payment: " + e.getMessage());
        }
    }

    @PostMapping("/service/pack/payment")
    public CoreApiResponse<ListTransactionRest> createWalletServicePackPayment(@RequestBody PaymentServicePackRequest request) {
        try {
            ListTransactionRest response = walletService.createWalletServicePackPayment(request);
            return CoreApiResponse.success(response, "Service pack payment created successfully.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Failed to create service pack payment: " + e.getMessage());
        }
    }
}
