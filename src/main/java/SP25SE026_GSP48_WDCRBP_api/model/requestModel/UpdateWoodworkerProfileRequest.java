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

    @Size(max = 2000, message = "Chính sách bảo hành không được vượt quá 2000 ký tự")
    private String warrantyPolicy;

    @Min(value = 0, message = "Rating không hợp lệ")
    @Max(value = 5, message = "Rating không được vượt quá 5")
    private int rating;

    @Size(max = 255, message = "Trạng thái xác minh không được vượt quá 255 ký tự")
    private String verificationStauts;

    @Size(max = 255, message = "Số đơn hàng không được vượt quá 255 ký tự")
    private String noOrder;

    @Size(max = 255, message = "Loại hình kinh doanh không được vượt quá 255 ký tự")
    private String businessType;

    @Size(max = 2000, message = "Link ảnh không được vượt quá 2000 ký tự")
    private String imgUrl;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Size(max = 50, message = "Mã phường không được vượt quá 50 ký tự")
    private String wardCode;

    @Size(max = 50, message = "Mã quận không được vượt quá 50 ký tự")
    private String districtId;

    @Size(max = 50, message = "Mã thành phố không được vượt quá 50 ký tự")
    private String cityId;

    @Min(value = 0, message = "Tổng sao không hợp lệ")
    private Short totalStar;

    @Min(value = 0, message = "Tổng đánh giá không hợp lệ")
    private Short totalReviews;
}
