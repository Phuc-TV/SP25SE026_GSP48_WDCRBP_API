package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpSendRest {
    private String status;
    private String message;

    public OtpSendRest(String error, String message, Object o) {
    }

    public OtpSendRest() {

    }
}
