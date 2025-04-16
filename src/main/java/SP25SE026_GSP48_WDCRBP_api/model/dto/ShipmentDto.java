package SP25SE026_GSP48_WDCRBP_api.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {
    private Long shipmentId;

    private String toAddress;

    private int toDistrictId;

    private String toWardCode;

    private String fromAddress;

    private int fromDistrictId;

    private String fromWardCode;

    private Integer ghnServiceId;

    private Integer ghnServiceTypeId;

    private String shippingUnit;

    private float fee;

    private String orderCode;

    private String shipType;
}
