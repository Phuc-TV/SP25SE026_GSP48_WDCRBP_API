package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.RequestedProductPersonalizeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceOrderPersonalizeRequest {
    private Long availableServiceId;
    private Long userId;
    private String address;
    private List<RequestedProductPersonalizeDto> requestedProducts;
}
