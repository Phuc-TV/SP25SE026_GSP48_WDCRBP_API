package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginOtpRest {
    private String status;
    private String message;
}
