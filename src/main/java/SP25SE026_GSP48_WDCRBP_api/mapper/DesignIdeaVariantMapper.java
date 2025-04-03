package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantConfigDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariantConfig;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.CategoryDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignVariantDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileDetailRes;

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

    public static DesignVariantDetailRes toDto(DesignIdeaVariant variant, List<DesignIdeaVariantConfig> configs, DesignIdea designIdea) {
        if (variant == null) return null;

        DesignVariantDetailRes dto = new DesignVariantDetailRes();
        dto.setDesignIdeaVariantId(variant.getDesignIdeaVariantId());
        dto.setPrice(variant.getPrice());

        List<DesignIdeaVariantConfigDto> configDtos = configs.stream()
                .map(DesignIdeaVariantConfigMapper::toDto)
                .collect(Collectors.toList());

        dto.setDesignIdeaVariantConfig(configDtos);
        dto.setDesignIdeaId(designIdea.getDesignIdeaId());
        dto.setName(designIdea.getName());
        dto.setImg_urls(designIdea.getImg_urls());
        dto.setDescription(designIdea.getDescription());
        dto.setTotalStar(designIdea.getTotalStar());
        dto.setTotalReviews(designIdea.getTotalReviews());

        Category category = designIdea.getCategory();
        CategoryDetailRes categoryDetailRes = new CategoryDetailRes();
        categoryDetailRes.setCategoryId(category.getCategoryId());
        categoryDetailRes.setCategoryName(category.getCategoryName());
        categoryDetailRes.setCategoryLevel(String.valueOf(category.getCategoryLevel()));

        WoodworkerProfileDetailRes wwDetailRes = WoodworkerProfileMapper.toDetailRes(designIdea.getWoodworkerProfile());
        dto.setCategory(categoryDetailRes);
        return dto;
    }
}
