package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateFeeRequest {
    private Integer from_district_id;
    private String from_ward_code;
    private Integer service_id;
    private Integer service_type_id;
    private Integer to_district_id;
    private String to_ward_code;
    private Integer insurance_value;
    private Integer cod_failed_amount;
    private String coupon;
    private Integer height;
    private Integer length;
    private Integer width;
    private Integer weight;
    private List<ItemDTO> items;
}
