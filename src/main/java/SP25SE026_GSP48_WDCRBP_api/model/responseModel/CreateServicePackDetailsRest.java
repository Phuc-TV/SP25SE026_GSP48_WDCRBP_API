package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServicePackDetailsRest {

    private String status;
    private String message;
    private Data data;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Long servicePackDetailsId;
        private Short postLimitPerMonth;
        private Boolean productManagement;
        private Short searchResultPriority;
        private Boolean personalization;
        private Long servicePackId;
    }
}
