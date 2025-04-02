package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoodworkerProfileListItemRes {
    private Long woodworkerId;

    private String brandName;

    private String imgUrl;

    private Short totalStar;

    private Short totalReviews;

    private String address;

    private String cityId;

    private ServicePack servicePack;

    private LocalDateTime servicePackEndDate;
}
