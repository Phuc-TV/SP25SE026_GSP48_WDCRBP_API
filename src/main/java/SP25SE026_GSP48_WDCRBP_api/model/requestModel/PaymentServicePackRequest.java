package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentServicePackRequest {
    @NotEmpty(message = "User ID cannot be empty")
    private String userId;

    @NotEmpty(message = "Service Pack ID cannot be empty")
    private String servicePackId;

    @NotEmpty(message = "Email cannot be empty")
    @Email(regexp = ".+@.+\\..+", message = "Email format is invalid")
    private String email;

    @NotEmpty(message = "Return URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Return URL must be a valid URL")
    private String returnUrl;
}
