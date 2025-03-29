package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentWalletRequest {
    @NotEmpty(message = "User ID cannot be empty")
    private String userId;

    @NotEmpty(message = "Wallet ID cannot be empty")
    private String walletId;

    private String transactionType;

    @Min(value = 1, message = "Amount must be greater than 0")
    @Max(value = 1_000_000_000, message = "Amount cannot exceed 1,000,000,000")
    private long amount;

    @NotEmpty(message = "Email cannot be empty")
    @Email(regexp = ".+@.+\\..+", message = "Email format is invalid")
    private String email;
}
