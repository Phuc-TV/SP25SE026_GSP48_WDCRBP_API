package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddServiceOrderRequest {
    private Long availableServiceId;
    private Long userId;
    private Long designIdeaVariantId;
    private Short quantity;
}
