package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantAppointmentDetailRes {
    private Long appointmentId;

    private LocalDateTime dateTime;

    private LocalDateTime createdAt;

    private String form;

    private String meetAddress;

    private String content;

    private boolean status;
}
