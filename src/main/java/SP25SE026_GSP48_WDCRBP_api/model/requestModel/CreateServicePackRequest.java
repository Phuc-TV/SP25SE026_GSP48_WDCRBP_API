package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;

@Getter
@Setter
public class CreateServicePackRequest {

    @NotBlank(message = "Service pack name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Float price;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    private Short duration;

    @NotNull(message = "PostLimitPerMonth is required")
    @Positive(message = "PostLimitPerMonth must be greater than 0")
    private Short postLimitPerMonth;

    @NotNull(message = "ProductManagement is required")
    private Boolean productManagement;

    @NotNull(message = "SearchResultPriority is required")
    private Short searchResultPriority;

    @NotNull(message = "Personalization is required")
    private Boolean personalization;

    private Boolean status;
}
