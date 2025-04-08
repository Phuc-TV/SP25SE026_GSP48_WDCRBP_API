package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.AvaliableServiceDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;

import java.util.List;

public class ServiceOrderMapper {
    public static ServiceOrderDto toServiceOrderDto(ServiceOrder entity, AvaliableServiceDto avaliableServiceDto, UserDetailRes userDetailRes) {
        if (entity == null) {
            return null;
        }

        ServiceOrderDto dto = new ServiceOrderDto();
        dto.setOrderId(entity.getOrderId());
        dto.setQuantity(entity.getQuantity() != null ? entity.getQuantity().byteValue() : null);
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setInstall(entity.isInstall());
        dto.setDescription(entity.getDescription());
        dto.setFeedback(entity.getFeedback());
        dto.setAmountPaid(entity.getAmountPaid());
        dto.setAmountRemaining(entity.getAmountRemaining());
        dto.setStatus(entity.getStatus());
        dto.setService(avaliableServiceDto);
        dto.setUser(userDetailRes);
        dto.setRole(entity.getRole());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public static ServiceOrderDetailRes toServiceOrderDetailRes(ServiceOrderDto serviceOrderDto, List<RequestedProductDetailRes> products, ConsultantAppointmentDetailRes consultantAppointmentDetailRes, ReviewRes reviewRes) {
        if (serviceOrderDto == null) {
            return null;
        }

        ServiceOrderDetailRes dto = new ServiceOrderDetailRes();
        dto.setOrderId(serviceOrderDto.getOrderId());
        dto.setQuantity(serviceOrderDto.getQuantity());
        dto.setTotalAmount(serviceOrderDto.getTotalAmount());
        dto.setInstall(serviceOrderDto.isInstall());
        dto.setDescription(serviceOrderDto.getDescription());
        dto.setStatus(serviceOrderDto.getStatus());
        dto.setFeedback(serviceOrderDto.getFeedback());
        dto.setAmountPaid(serviceOrderDto.getAmountPaid());
        dto.setAmountRemaining(serviceOrderDto.getAmountRemaining());
        dto.setRole(serviceOrderDto.getRole());
        dto.setService(serviceOrderDto.getService());
        dto.setUser(serviceOrderDto.getUser());
        dto.setRequestedProduct(products);
        dto.setConsultantAppointment(consultantAppointmentDetailRes);
        dto.setReview(reviewRes);
        dto.setCreatedAt(serviceOrderDto.getCreatedAt());

        return dto;
    }
}