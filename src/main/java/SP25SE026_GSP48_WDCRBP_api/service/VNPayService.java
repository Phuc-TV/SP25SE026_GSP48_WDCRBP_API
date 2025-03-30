package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentWalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PaymentRest;

public interface VNPayService {
    PaymentRest processOrderPayment(PaymentOrderRequest request);
    PaymentRest processServicePackPayment(PaymentServicePackRequest request);
    PaymentRest processWalletPayment(PaymentWalletRequest request);
}
