package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateWarrantyPolicyByWwIdRequest {

    @NotNull(message = "WoodworkerId must not be null")
    private Long woodworkerId;

    @NotBlank(message = "Warranty policy must not be blank")
    @Size(max = 2000, message = "Warranty policy must be less than 2000 characters")
    private String warrantyPolicy;
}
