// Request DTO for banning a user
package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BanUserRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Ban status must be specified")
    private Boolean banned;
}
