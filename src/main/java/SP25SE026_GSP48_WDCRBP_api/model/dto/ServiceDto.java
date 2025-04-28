package SP25SE026_GSP48_WDCRBP_api.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long serviceId;

    private String serviceName;

    private boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Short numberOfDeposits;
}
