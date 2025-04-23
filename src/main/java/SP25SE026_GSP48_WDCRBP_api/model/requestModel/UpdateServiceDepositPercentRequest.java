package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceDepositPercentRequest {

    @NotNull(message = "Service ID must not be null")
    private Long serviceId;

    @NotNull(message = "Deposits list must not be null")
    private List<DepositUpdateEntry> deposits;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepositUpdateEntry {
        @NotNull(message = "ServiceDepositId must not be null")
        private Long serviceDepositId;

        @NotNull(message = "New percent must not be null")
        private Short newPercent;
    }
}
