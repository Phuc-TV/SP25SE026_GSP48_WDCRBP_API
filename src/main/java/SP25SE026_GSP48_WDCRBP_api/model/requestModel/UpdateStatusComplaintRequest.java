package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusComplaintRequest {

    @NotNull(message = "Service Order ID cannot be null")
    @Positive(message = "Service Order ID must be a positive number")
    private Long serviceOrderId;

    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;
}
