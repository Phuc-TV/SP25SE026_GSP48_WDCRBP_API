package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentOrderRequest implements Serializable {

    @NotEmpty(message = "User ID cannot be empty")
    private String userId;

    @NotEmpty(message = "Order Deposit ID cannot be empty")
    private String orderDepositId;

    private String transactionType;

    @NotEmpty(message = "Email cannot be empty")
    @Email(regexp = ".+@.+\\..+", message = "Email format is invalid")
    private String email;

    @NotEmpty(message = "Return URL cannot be empty")
    @Pattern(regexp = "^(http|https)://.*$", message = "Return URL must be a valid URL")
    private String returnUrl;
}
