package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaConfigValueDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfigValue;

public class DesignIdeaConfigValueMapper {
    public static DesignIdeaConfigValueDto toDto(DesignIdeaConfigValue value) {
        if (value == null) return null;

        DesignIdeaConfigValueDto dto = new DesignIdeaConfigValueDto();
        dto.setDesignIdeaConfigValueId(value.getDesignIdeaConfigValueId());
        dto.setValue(value.getValue());
        dto.setDesignIdeaConfig(DesignIdeaConfigMapper.toDto(value.getDesignIdeaConfig()));
        return dto;
    }
}
