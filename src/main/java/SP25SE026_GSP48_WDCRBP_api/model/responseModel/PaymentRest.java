package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRest implements Serializable {
    private String status;
    private String message;
    private String URL;
    private String expirationTime;
    private String expirationDate;
    private String timeZone;
}
