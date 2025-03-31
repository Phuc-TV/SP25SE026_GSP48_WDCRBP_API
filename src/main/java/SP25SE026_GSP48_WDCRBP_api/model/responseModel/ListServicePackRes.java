package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListServicePackRes {
    private List<Data> data;

    @Getter
    @Setter
    public static class Data {
        private Long servicePackId;
        private String name;
        private Float price;
        private String description;
        private Short duration;
        private Short postLimitPerMonth;
        private Boolean productManagement;
        private Short searchResultPriority;
        private Boolean personalization;
    }
}
