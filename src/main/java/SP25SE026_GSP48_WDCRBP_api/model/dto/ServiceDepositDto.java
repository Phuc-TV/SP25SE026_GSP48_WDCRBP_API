package SP25SE026_GSP48_WDCRBP_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDepositDto {
    private Long orderDepositId;

    private Short depositNumber;

    private Short percent;

    private Float amount;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
