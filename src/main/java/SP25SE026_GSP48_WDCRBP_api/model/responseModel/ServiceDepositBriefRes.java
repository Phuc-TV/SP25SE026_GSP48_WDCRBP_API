package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Data;

@Data
public class ServiceDepositBriefRes {
    private Long serviceDepositId;
    private Short percent;
    private Short depositNumber;
    private String description;
}
