package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WalletRest;

public interface WalletService {
    WalletRest getWalletByUserId(Long userId);
    WalletRest updateBalanceWallet(WalletRequest walletRequest);
    ListTransactionRest createWalletOrderPayment(PaymentOrderRequest request);
    ListTransactionRest createWalletServicePackPayment(PaymentServicePackRequest request);
}
