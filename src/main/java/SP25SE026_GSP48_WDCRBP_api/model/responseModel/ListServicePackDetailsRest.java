package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListServicePackDetailsRest {
    private String status;
    private String message;
    private List<Data> data;

    @Getter
    @Setter
    public static class Data {
        private Long servicePackDetailsId;
        private Short postLimitPerMonth;
        private Boolean productManagement;
        private Short searchResultPriority;
        private Boolean personalization;
        private Long servicePackId;
    }
}
