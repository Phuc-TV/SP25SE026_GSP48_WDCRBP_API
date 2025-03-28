package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWoodworkerServicePackRequest {
    @NotNull(message = "Woodworker ID is required")
    private Long woodworkerId;

    @NotNull(message = "Service Pack ID is required")
    private Long servicePackId;
}
