package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResetPasswordRest {
    private String status;
    private String message;

    public ResetPasswordRest(String error, String message, Object o) {
    }

    public ResetPasswordRest() {

    }
}
