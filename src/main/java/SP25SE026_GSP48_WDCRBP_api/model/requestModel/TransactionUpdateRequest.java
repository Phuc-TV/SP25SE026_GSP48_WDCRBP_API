package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionUpdateRequest {

    @NotNull(message = "Transaction ID cannot be null")
    private Long transactionId;

    @Null(message = "Status cannot be provided as null. Please provide true/false values only.")
    private Boolean status;

    @FutureOrPresent(message = "Canceled At date must be in the future or present")
    private LocalDateTime canceledAt;
}
