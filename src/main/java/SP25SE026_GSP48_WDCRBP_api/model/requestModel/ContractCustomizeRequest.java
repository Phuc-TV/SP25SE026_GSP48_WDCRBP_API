package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractCustomizeRequest {
    private String woodworkerSignature;

    private String warrantyPolicy;

    private LocalDateTime completeDate;

    private LocalDateTime warrantyPeriod;

    private Long serviceOrderId;

    private Float totalAmount;
}
