package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignIdeaDetailRes {
    private Long designIdeaId;

    private String name;

    private String img_urls;

    private Boolean isInstall;

    private String description;

    private Short totalStar;

    private Short totalReviews;

    private WoodworkerProfileDetailRes woodworkerProfile;

    private CategoryDetailRes category;
}
