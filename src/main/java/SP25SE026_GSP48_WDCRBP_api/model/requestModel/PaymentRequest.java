package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentRequest implements Serializable {
    private String userId;
    private String orderDepositId;
    private long amount;
    private String email;
}
