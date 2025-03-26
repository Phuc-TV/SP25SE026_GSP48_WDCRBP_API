package SP25SE026_GSP48_WDCRBP_api.model.dto;

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
public class WoodworkerProfileDto {
    private Long woodworkerId;

    private String brandName;

    private String bio;

    private String imgUrl;

    private int rating;

    private Short totalStar;

    private Short totalReviews;

    private User user;

    private ServicePack servicePack;
}
