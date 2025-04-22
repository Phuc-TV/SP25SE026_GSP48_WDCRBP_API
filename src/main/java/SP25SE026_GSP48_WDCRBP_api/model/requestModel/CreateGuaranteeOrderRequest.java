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
public class CreateGuaranteeOrderRequest {
    private Long availableServiceId;
    private String toDistrictId;
    private String toWardCode;
    private int ghnServiceId;
    private int ghnServiceTypeId;
    private String note;
    private Long userId;
    private String address;
    private Long requestedProductId;
    private Boolean isInstall;
    private int priceShipping;
    private String productCurrentStatus;
    private String currentProductImgUrls;
    private String guaranteeError;
    private Boolean isGuarantee;
}
