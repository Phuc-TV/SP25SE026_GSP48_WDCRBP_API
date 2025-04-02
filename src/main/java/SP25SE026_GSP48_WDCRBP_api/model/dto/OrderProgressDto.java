package SP25SE026_GSP48_WDCRBP_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProgressDto {
    private Long progressId;

    private String description;

    private Long createdBy;

    private Long deletedBy;

    private LocalDateTime createdTime;

    private LocalDateTime deletedTime;

    private String status;
}
