package SP25SE026_GSP48_WDCRBP_API.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WDCRBPApiException extends RuntimeException {
    @Getter
    private HttpStatus status;

    @Getter
    private String message;

    public WDCRBPApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public WDCRBPApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}
