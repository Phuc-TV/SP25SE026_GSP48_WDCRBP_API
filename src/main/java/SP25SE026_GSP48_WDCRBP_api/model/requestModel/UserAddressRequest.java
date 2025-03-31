package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {

    @NotNull(message = "Trạng thái mặc định không được để trống")
    private Boolean isDefault;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Mã phường/xã không được để trống")
    private String wardCode;

    @NotBlank(message = "Mã quận/huyện không được để trống")
    private String districtId;

    @NotBlank(message = "Mã tỉnh/thành phố không được để trống")
    private String cityId;

    @NotNull(message = "Mã người dùng không được để trống")
    private Long userId;
}
