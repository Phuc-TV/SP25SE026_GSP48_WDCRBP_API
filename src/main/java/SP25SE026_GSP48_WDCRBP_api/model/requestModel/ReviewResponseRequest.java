package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseRequest {
    private Long reviewId;
    private String woodworkerResponse;
}
