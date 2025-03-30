package SP25SE026_GSP48_WDCRBP_api.model.dto;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoodworkerProfileListResponseDto {
    private Long woodworkerId;

    private String brandName;

    private String imgUrl;

    private Short totalStar;

    private Short totalReviews;

    private String address;

    private User user;

    private ServicePack servicePack;
}
