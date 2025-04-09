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
    private Integer fromDistrictId;
    private String fromWardCode;
    private Integer serviceId;
    private Integer serviceTypeId;
    private Integer toDistrictId;
    private String toWardCode;
    private Integer insuranceValue;
    private Integer codFailedAmount;
    private String coupon;
    private Integer height;
    private Integer length;
    private Integer width;
    private Integer weight;
    private List<ItemDTO> items;
}
