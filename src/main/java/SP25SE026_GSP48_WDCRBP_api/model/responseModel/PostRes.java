package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRes {
    private Long postId;
    private String title;
    private String description;
    private String imgUrls;
    private LocalDateTime createdAt;
    private Long woodworkerId;
    private String woodworkerName;
}
