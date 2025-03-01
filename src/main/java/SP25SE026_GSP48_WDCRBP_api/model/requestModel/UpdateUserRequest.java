package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotEmpty(message = "UserId should not be empty!")
    private String userId;
    @NotEmpty(message = "Username should not be empty!")
    private String username;
    @NotEmpty(message = "Phone should not be empty!")
    private String phone;
    @NotEmpty(message = "Email should not be empty!")
    private String email;
    @NotEmpty(message = "Address should not be empty!")
    private String address;
    @NotEmpty(message = "WardCode should not be empty!")
    private String wardCode;
    @NotEmpty(message = "DistrictId should not be empty!")
    private int districtId;
    @NotEmpty(message = "CityId should not be empty!")
    private int cityId;
}
