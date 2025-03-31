package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WalletRes;

public interface WalletService {
    WalletRes getWalletByUserId(Long userId);
    WalletRes updateBalanceWallet(WalletRequest walletRequest);
    ListTransactionRes createWalletOrderPayment(PaymentOrderRequest request);
    ListTransactionRes createWalletServicePackPayment(PaymentServicePackRequest request);
}
