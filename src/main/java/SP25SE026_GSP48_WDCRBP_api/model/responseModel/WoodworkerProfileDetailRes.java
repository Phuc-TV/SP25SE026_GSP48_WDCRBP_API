package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoodworkerProfileDetailRes {
    private Long woodworkerId;

    private String brandName;

    private String bio;

    private String businessType;

    private String taxCode;

    private String imgUrl;

    private String address;

    private String wardCode;

    private String warrantyPolicy;

    private String districtId;

    private String cityId;

    private Short totalStar;

    private Short totalReviews;

    private LocalDateTime servicePackStartDate;

    private LocalDateTime servicePackEndDate;

    private UserDetailRes user;

    private ServicePack servicePack;

    private Boolean publicStatus;
}
