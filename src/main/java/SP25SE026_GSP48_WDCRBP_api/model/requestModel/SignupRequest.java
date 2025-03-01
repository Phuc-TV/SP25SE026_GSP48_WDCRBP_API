package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotEmpty(message = "Username should not be empty!")
    private String username;

    @NotEmpty(message = "Password should not be empty!")
    private String password;

    @Email(regexp = ".+@.+\\..+", message = "Email is invalid!")
    private String email;

    @NotEmpty(message = "Phone number should not be empty!")
    @Pattern(regexp="(^$|[0-9]{10,11})", message = "Phone number must be 10 or 11 digits!")
    private String phone;
}
