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
public class ResetPasswordOTPRequest {

    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;

    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;
}
