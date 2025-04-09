package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WwCreateContractCustomizeRequest {
    private String woodworkerSignature;

    private String woodworkerTerms;

    private String warrantyPolicy;

    private LocalDateTime completeDate;

    private LocalDateTime warrantyPeriod;

    private Long serviceOrderId;
}
