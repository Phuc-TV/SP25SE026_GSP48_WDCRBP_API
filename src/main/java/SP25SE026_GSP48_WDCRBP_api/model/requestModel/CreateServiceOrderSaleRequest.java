package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantCusDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ProductSaleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceOrderSaleRequest {
    private Long availableServiceId;
    private String toDistrictId;
    private String toWardCode;
    private int ghnServiceId;
    private int ghnServiceTypeId;
    private Long userId;
    private List<ProductSaleDto> productIds;
    private String address;
    private String description;
    private Boolean isInstall;
    private int priceShipping;
}
