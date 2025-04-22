package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusComplaintRes {
    private Long complaintId;
    private String updatedStatus;
    private String message;
}
