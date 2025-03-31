package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantConfigDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfigValue;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariantConfig;

import java.util.Collections;

public class DesignIdeaVariantConfigMapper {
    public static DesignIdeaVariantConfigDto toDto(DesignIdeaVariantConfig config) {
        if (config == null) return null;

        DesignIdeaVariantConfigDto dto = new DesignIdeaVariantConfigDto();
        dto.setDesignIdeaVariantConfigId(config.getDesignIdeaVariantConfigId());

        DesignIdeaConfigValue value = config.getDesignIdeaConfigValue();
        if (value != null) {
            dto.setDesignVariantValues(Collections.singletonList(
                    DesignIdeaConfigValueMapper.toDto(value)
            ));
        }

        return dto;
    }
}
