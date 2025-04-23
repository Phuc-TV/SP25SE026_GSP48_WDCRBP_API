package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationUpdateRequest {

    private Long configurationId; // required for update

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Value is required")
    private String value;

    @NotBlank(message = "Tên cấu hình không được để trống")
    private String name;

}
