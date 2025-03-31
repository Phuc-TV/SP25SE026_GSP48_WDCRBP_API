package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotNull(message = "Category ID must not be null")
    private Long parentId;

    @NotBlank(message = "Category name must not be blank")
    private String categoryName;

    private String description;

    @NotBlank(message = "Category level must not be blank")
    private String categoryLevel;

    @NotNull(message = "Status must not be null")
    private Boolean status;

}
