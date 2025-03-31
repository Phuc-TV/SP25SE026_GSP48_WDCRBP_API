package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentWalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PaymentRes;

public interface VNPayService {
    PaymentRes processOrderPayment(PaymentOrderRequest request);
    PaymentRes processServicePackPayment(PaymentServicePackRequest request);
    PaymentRes processWalletPayment(PaymentWalletRequest request);
}
