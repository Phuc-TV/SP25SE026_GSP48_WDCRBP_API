package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRes {
    private Long categoryId;
    private Long parentId;
    private String categoryName;
    private String description;
    private String categoryLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean status;
}
