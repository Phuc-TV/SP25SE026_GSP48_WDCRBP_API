package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WalletRes {
    private Long walletId;
    private Float balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
