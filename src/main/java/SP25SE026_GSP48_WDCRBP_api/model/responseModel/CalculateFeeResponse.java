package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculateFeeResponse {
    private int code;
    private String message;
    private Data data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data {
        private int total;
        private int serviceFee;
        private int insuranceFee;
        private int pickStationFee;
        private int couponValue;
        private int r2sFee;
        private int documentReturn;
        private int doubleCheck;
        private int codFee;
        private int pickRemoteAreasFee;
        private int deliverRemoteAreasFee;
        private int codFailedFee;
    }
}
