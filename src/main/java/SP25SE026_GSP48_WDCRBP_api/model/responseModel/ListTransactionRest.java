package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.entity.PaymentMethod;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Wallet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListTransactionRest {
    private List<Data> data;
    @Getter
    @Setter
    public static class Data {
        private Long transactionId;
        private String transactionType;
        private float amount;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime canceledAt;
        private boolean status;
        private Long userId;
        private Long orderDepositId;
        private Long paymentId;
        private Long walletId;
    }
}
