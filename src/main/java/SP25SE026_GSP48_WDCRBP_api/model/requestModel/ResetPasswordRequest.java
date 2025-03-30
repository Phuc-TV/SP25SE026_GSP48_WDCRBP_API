package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Email is required")
    @Email(regexp = ".+@.+\\..+", message = "Email is invalid!")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;
}
