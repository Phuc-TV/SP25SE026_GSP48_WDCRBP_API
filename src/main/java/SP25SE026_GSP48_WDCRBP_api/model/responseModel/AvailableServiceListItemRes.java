package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Service;
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
public class AvailableServiceListItemRes {
    private Long availableServiceId;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean status;

    private Boolean operatingStatus;

    private Service service;
}
