package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationRes {
    private Long configurationId;
    private String description;
    private String value;
    private String name;
    private LocalDateTime createdBy;
    private LocalDateTime updatedAt;
}
