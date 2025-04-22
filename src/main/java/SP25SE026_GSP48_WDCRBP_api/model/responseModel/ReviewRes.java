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
    private Long woodworkerId;
    private String brandName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String woodworkerResponse;
    private Boolean woodworkerResponseStatus;
    private Boolean status;
    private LocalDateTime responseAt;
    private String serviceName;
    private Long serviceOrderId;
    private Long guaranteeOrderId;
}
