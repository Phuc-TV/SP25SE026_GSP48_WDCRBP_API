package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Data;

import java.util.List;

@Data
public class ServiceWithDepositsRes {
    private ServiceBasicInfo service;
    private List<ServiceDepositBriefRes> serviceDeposits;

    @Data
    public static class ServiceBasicInfo {
        private Long serviceId;
        private String serviceName;
    }
}
