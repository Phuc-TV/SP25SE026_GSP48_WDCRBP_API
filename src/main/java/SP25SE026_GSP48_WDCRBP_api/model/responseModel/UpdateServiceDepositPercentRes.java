package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDepositPercentRes {
        private Short depositNumber;
        private Short percent;
        private String description;
        private Boolean status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long serviceId;
    }

