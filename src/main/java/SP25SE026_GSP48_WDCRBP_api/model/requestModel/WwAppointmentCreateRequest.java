package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WwAppointmentCreateRequest {
    private Long serviceOrderId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeMeeting;

    private String meetAddress;

    private String note;

    private String form;

    private String desc;
}
