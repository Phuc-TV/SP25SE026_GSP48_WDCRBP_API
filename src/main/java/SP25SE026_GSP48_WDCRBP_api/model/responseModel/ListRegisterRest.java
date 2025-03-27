package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListRegisterRest {
    private String status;
    private String message;
    private List<Data> data;  // ✅ should be a list (not a single object)

    @Getter
    @Setter
    public static class Data {
        private Long woodworkerId;
        private Long userId;

        private String brandName;
        private String bio;
        private String imgUrl;
        private String businessType;
        private String taxCode;
        private String status;

        private String address;
        private String wardCode;
        private String districtId;
        private String cityId;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
