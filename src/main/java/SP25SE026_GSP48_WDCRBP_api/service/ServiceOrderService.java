package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ProductImagesDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ProductImages;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderSaleRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderListItemRes;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceOrderService {
    List<ServiceOrderDto> listServiceOrderByUserIdOrWwId(Long id, String role);

    List<ServiceOrderListItemRes> getAll();

    ServiceOrderDetailRes getServiceDetailById(Long id);

    CoreApiResponse addServiceOrderCustomize(CreateServiceOrderCusRequest createServiceOrderCusRequest);

    void addSaleOrder(CreateServiceOrderSaleRequest request);

    ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting, String form, String desc);

    ServiceOrder customerFeedback(Long serviceOrderId, String feedback);

    ServiceOrder createServiceOrderPersonalize(CreateServiceOrderPersonalizeRequest createServiceOrderPersonalizeRequest);

    void addProductImage(List<ProductImagesDto> productImagesDtos, Long serviceId);

    void addProductFinishImage(List<ProductImagesDto> productImagesDtos, Long serviceId);

    void cancelOrder(Long id);
}
