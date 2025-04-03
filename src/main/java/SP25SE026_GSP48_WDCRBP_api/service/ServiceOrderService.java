package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ProductImagesDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ProductImages;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderPersonalizeRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateServiceOrderCusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ServiceOrderDetailRes;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceOrderService {
    List<ServiceOrderDto> listServiceOrderByUserIdOrWwId(Long id, String role);

    ServiceOrderDetailRes getServiceDetailById(Long id);

    CoreApiResponse addServiceOrderCustomize(CreateServiceOrderCusRequest createServiceOrderCusRequest);

    ServiceOrder acceptServiceOrder(Long serviceOrderId, LocalDateTime timeMeeting, String linkMeeting);

    ServiceOrder customerFeedback(Long serviceOrderId, String feedback);

    ServiceOrder createServiceOrderPersonalize(CreateServiceOrderPersonalizeRequest createServiceOrderPersonalizeRequest);

    List<ProductImages> addProductImage(List<ProductImagesDto> productImagesDtos);
}
