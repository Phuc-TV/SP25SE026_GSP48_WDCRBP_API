package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateComplaintRequest {
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Service Order ID cannot be null")
    @Positive(message = "Service Order ID must be a positive number")
    private Long serviceOrderId;

    private String complaintType;

    private String proofImgUrls;
}
