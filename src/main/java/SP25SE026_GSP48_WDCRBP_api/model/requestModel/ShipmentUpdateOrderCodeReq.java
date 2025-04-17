package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentUpdateOrderCodeReq {
    private String orderCode;
    private String type;
}
