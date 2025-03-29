package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WoodworkerRequest {

    @NotEmpty(message = "Full Name is required")
    @Size(max = 255, message = "Full Name cannot be more than 255 characters")
    private String fullName;

    @NotEmpty(message = "Email is required")
    @Email(regexp = ".+@.+\\..+", message = "Email format is invalid")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should be 10 digits")
    private String phone;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "Ward code is required")
    private String wardCode;

    @NotEmpty(message = "District id is required")
    private String districtId;

    @NotEmpty(message = "City id is required")
    private String cityId;

    @NotEmpty(message = "Business Type is required")
    private String businessType;

    @NotEmpty(message = "Tax code is required")
    private String taxCode;

    @NotEmpty(message = "Brand name is required")
    private String brandName;

    @NotEmpty(message = "Bio is required")
    private String bio;

    @NotEmpty(message = "Profile picture is required")
    private String imgUrl;
}
