package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusPublicRes {
    private Long woodworkerId;
    private boolean updatedPublicStatus;
    private String message;
}
