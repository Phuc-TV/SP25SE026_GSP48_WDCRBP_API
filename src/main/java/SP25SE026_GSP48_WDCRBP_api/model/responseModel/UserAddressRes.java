package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRes {
    private Long userAddressId;
    private Boolean isDefault;
    private String address;
    private String wardCode;
    private String districtId;
    private String cityId;
    private LocalDateTime createdAt;
    private Long userId;
}
