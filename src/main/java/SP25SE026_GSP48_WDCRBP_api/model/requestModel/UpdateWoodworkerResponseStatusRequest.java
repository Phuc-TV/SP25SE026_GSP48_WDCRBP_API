package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWoodworkerResponseStatusRequest {
    @NotNull
    private Boolean woodworkerResponseStatus;
}
