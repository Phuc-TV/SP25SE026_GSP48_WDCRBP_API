package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WalletRequest {
    @NotNull(message = "Wallet Id cannot be null")
    private Long walletId;

    @Min(value = 1, message = "Amount must be greater than 0")
    @Max(value = 1_000_000_000, message = "Amount cannot exceed 1,000,000,000")
    private Float amount;
}
