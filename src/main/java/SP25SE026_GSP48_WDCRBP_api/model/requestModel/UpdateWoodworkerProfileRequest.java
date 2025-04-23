package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWoodworkerProfileRequest {

    @NotNull(message = "woodworkerId không được để trống")
    private Long woodworkerId;

    @Size(max = 255, message = "Tên thương hiệu không được vượt quá 255 ký tự")
    private String brandName;

    @Size(max = 2000, message = "Giới thiệu không được vượt quá 2000 ký tự")
    private String bio;

    @Size(max = 2000, message = "Link ảnh không được vượt quá 2000 ký tự")
    private String imgUrl;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Size(max = 50, message = "Mã phường không được vượt quá 50 ký tự")
    private String wardCode;

    @Size(max = 50, message = "Loại hình kinh doan không được vượt qu 50 ký tự")
    private String businessType;

    @Size(max = 50, message = "Mã quận không được vượt quá 50 ký tự")
    private String districtId;

    @Size(max = 50, message = "Mã thành phố không được vượt quá 50 ký tự")
    private String cityId;
}
