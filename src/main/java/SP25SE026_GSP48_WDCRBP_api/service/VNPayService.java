package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRest;

public interface VNPayService {
    PaymentRest processPayment(PaymentRequest request);
}
