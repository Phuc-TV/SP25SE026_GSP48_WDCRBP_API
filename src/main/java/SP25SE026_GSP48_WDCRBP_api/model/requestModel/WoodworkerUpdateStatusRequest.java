package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WoodworkerUpdateStatusRequest {

    @NotEmpty(message = "Woodworker ID is required")
    private String woodworkerId;

    @NotNull(message = "Status is required")
    private boolean status;

    private String Description;
}
