package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationSearchRequest {
    private String description;
    private String value;
}
