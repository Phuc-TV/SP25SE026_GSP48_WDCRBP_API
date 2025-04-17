package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateGuaranteeOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.GuaranteeOrderDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.GuaranteeOrderListItemRes;

import java.time.LocalDateTime;
import java.util.List;

public interface GuaranteeOrderService {
    GuaranteeOrderListItemRes createGuaranteeOrder(CreateGuaranteeOrderRequest request);

    List<GuaranteeOrderListItemRes> listGuaranteeOrderByUserIdOrWwId(Long id, String role);

    GuaranteeOrderDetailRes getGuaranteeDetailById(Long id);

    void acceptGuaranteeOrder(Long guaranteeOrderId, LocalDateTime timeMeeting, String linkMeeting, String form, String desc);

    void confirmReceiveProduct(Long guaranteeOrderId);

    void confirmFinishRepair(Long guaranteeOrderId);

    void customerFeedback(Long guaranteeOrderId, String feedback);
}