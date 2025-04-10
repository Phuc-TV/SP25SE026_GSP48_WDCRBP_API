package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetGHNAvailableServiceRequest {
    private int from_district;
    private int to_district;
    private int shop_id;
}
