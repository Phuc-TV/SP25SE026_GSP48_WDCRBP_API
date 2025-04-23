package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationNameRequest {

    @NotBlank(message = "Tên cấu hình không được để trống")
    private String name;
}
