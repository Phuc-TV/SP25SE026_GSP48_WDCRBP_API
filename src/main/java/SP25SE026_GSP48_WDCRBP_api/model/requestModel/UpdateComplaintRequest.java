package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateComplaintRequest {

    @NotNull(message = "complaintId cannot be null")
    @Positive(message = "complaintId must be a positive number")
    private Long complaintId;

    @NotBlank(message = "woodworkerResponse cannot be blank")
    @Size(max = 500, message = "woodworkerResponse cannot exceed 500 characters")
    private String woodworkerResponse;
}
