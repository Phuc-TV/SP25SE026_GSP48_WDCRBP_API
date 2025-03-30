package SP25SE026_GSP48_WDCRBP_api.model.dto;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoodworkerProfileDetailResponseDto {
    private Long woodworkerId;

    private String brandName;

    private String bio;

    private String businessType;

    private String taxCode;

    private String imgUrl;

    private String address;

    private String wardCode;

    private String districtId;

    private String cityId;

    private Short totalStar;

    private Short totalReviews;

    private LocalDateTime servicePackStartDate;

    private LocalDateTime servicePackEndDate;

    private User user;

    private ServicePack servicePack;
}
