package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServicePackDetailsRequest {

    @NotNull(message = "Post limit per month is required")
    @PositiveOrZero(message = "Post limit must be zero or positive")
    private Short postLimitPerMonth;

    @NotNull(message = "Product management setting is required")
    private Boolean productManagement;

    @NotNull(message = "Search result priority is required")
    @PositiveOrZero(message = "Search result priority must be zero or positive")
    private Short searchResultPriority;

    @NotNull(message = "Personalization setting is required")
    private Boolean personalization;

    @NotNull(message = "Service pack ID is required")
    @Positive(message = "Service pack ID must be a positive number")
    private Long servicePackId;
}
