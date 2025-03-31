package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaConfigDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfig;

public class DesignIdeaConfigMapper {
    public static DesignIdeaConfigDto toDto(DesignIdeaConfig config) {
        if (config == null) return null;

        DesignIdeaConfigDto dto = new DesignIdeaConfigDto();
        dto.setDesignIdeaConfigId(config.getDesignIdeaConfigId());
        dto.setSpecifications(config.getSpecifications());
        return dto;
    }
}
