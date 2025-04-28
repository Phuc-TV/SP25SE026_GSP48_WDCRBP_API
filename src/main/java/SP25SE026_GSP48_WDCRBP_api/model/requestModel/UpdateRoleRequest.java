// Request DTO for updating role
package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Role cannot be null")
    private String newRole;
}
