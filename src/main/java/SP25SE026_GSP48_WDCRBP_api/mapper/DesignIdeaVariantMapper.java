package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantConfigDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariantConfig;

import java.util.List;
import java.util.stream.Collectors;

public class DesignIdeaVariantMapper {
    public static DesignIdeaVariantDto toDto(DesignIdeaVariant variant, List<DesignIdeaVariantConfig> configs) {
        if (variant == null) return null;

        DesignIdeaVariantDto dto = new DesignIdeaVariantDto();
        dto.setDesignIdeaVariantId(variant.getDesignIdeaVariantId());
        dto.setPrice(variant.getPrice());

        List<DesignIdeaVariantConfigDto> configDtos = configs.stream()
                .map(DesignIdeaVariantConfigMapper::toDto)
                .collect(Collectors.toList());

        dto.setDesignIdeaVariantConfig(configDtos);
        return dto;
    }
}
