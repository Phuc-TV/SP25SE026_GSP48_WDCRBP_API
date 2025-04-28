package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusComplaintRequest {

    @NotNull(message = "complaintId cannot be null")
    @Positive(message = "complaintId must be a positive number")
    private Long complaintId;

    private Float refundAmount;

    private Integer refundPercent;

    private Boolean isAccept;

    private String staffResponse;

    private Long staffUserId;
}
