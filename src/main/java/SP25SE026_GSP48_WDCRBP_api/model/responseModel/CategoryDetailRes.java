package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailRes {
    private Long categoryId;

    private Long parentId;

    private String categoryName;

    private String categoryLevel;
}
