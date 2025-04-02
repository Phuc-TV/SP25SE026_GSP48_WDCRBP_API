package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.AvaliableServiceDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;

public class ServiceOrderMapper {

    // Map từ Entity -> DTO
    public static ServiceOrderDto toDto(ServiceOrder entity, AvaliableServiceDto avaliableServiceDto ) {
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

        return dto;
    }
}