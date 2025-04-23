package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetWarrantyPolicyByWwIdRequest {

    @NotNull(message = "WoodworkerId must not be null")
    private Long woodworkerId;
}
