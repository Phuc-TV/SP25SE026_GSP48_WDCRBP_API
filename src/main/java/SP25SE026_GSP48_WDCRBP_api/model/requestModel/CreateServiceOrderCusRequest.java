package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantCusDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceOrderCusRequest {
    private Long availableServiceId;
    private Long userId;
    private List<DesignIdeaVariantCusDto> designIdeaVariantIds;
    private String address;
    private String description;
    private Boolean isInstall;
}
