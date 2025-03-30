package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WwCreateContractCustomizeResponse {
    private String woodworkerSignature;

    private String warrantyPolicy;

    private LocalDateTime completeDate;

    private LocalDateTime signDate;

    private LocalDateTime warrantyPeriod;

}
