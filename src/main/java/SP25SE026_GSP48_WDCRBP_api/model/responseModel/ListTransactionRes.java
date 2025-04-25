package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListTransactionRes {
    private List<Data> data;
    @Getter
    @Setter
    public static class Data {
        private Long transactionId;
        private String transactionType;
        private String paymentFor;
        private float amount;
        private String description;
        private LocalDateTime createdAt;
        private boolean status;
        private Long userId;
        private String username;
        private String userEmail;
        private String userPhone;
        private Long orderDepositId;
        private Long paymentId;
        private Long walletId;
        private String role;
    }
}
