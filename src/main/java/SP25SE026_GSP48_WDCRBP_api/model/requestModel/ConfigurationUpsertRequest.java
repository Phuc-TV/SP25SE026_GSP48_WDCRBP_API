package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationUpsertRequest {
    private Long configurationId; // required for update

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Value is required")
    private String value;
}
