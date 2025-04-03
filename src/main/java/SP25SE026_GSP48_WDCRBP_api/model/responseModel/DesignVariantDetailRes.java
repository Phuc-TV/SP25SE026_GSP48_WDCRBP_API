package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantConfigDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignVariantDetailRes {
    private Long designIdeaId;

    private String name;

    private String img_urls;

    private String description;

    private Short totalStar;

    private Short totalReviews;

    private WoodworkerProfileDetailRes woodworkerProfile;

    private CategoryDetailRes category;

    private Long designIdeaVariantId;

    private Float price;

    private List<DesignIdeaVariantConfigDto> designIdeaVariantConfig;
}
