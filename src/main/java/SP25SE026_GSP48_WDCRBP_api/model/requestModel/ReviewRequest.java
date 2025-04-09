package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Service order ID is required")
    @Positive(message = "Service order ID must be a positive number")
    private Long serviceOrderId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Size(max = 300, message = "Comment must not exceed 300 characters")
    private String comment;
}
