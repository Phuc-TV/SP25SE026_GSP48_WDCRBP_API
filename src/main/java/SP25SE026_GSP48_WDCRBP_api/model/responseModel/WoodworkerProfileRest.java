package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WoodworkerProfileRest {

    private Data data;

    @Getter
    @Setter
    public static class Data {
        private Long woodworkerId;
        private String brandName;
        private String bio;
        private String imgUrl;
        private String businessType;
        private String taxCode;
        private String address;
        private String wardCode;
        private String districtId;
        private String cityId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
