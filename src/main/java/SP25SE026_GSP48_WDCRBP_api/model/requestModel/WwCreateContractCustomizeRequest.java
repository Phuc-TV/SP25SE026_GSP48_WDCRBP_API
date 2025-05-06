package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WwCreateContractCustomizeRequest {
    private String woodworkerSignature;

    private String woodworkerTerms;

    private LocalDateTime completeDate;

    private Long serviceOrderId;

    private List<Long> requestedProductIds;

    private List<Short> warrantyDurations;

    private String agreement;
}
