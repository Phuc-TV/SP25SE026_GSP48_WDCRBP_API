package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRes {
    private Long reviewId;
    private Long userId;
    private String username;
    private String description;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String woodworkerResponse;
    private boolean woodworkerResponseStatus;
    private boolean status;
    private LocalDateTime responseAt;
    private String serviceName;
}
