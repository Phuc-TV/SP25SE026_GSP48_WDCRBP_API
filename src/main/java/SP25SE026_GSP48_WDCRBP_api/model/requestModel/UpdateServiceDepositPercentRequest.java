package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.Data;

@Data
public class UpdateServiceDepositPercentRequest {
    private Long serviceId;
    private Short newPercent;
}
