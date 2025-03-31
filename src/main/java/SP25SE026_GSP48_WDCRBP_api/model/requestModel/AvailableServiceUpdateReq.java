package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Service;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableServiceUpdateReq {
    private Long availableServiceId;

    private String description;

    private Boolean operatingStatus;
}
