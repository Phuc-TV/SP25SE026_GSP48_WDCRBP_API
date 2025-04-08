package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestedProductDetailRes {
    private Long requestedProductId;

    private Byte quantity;

    private LocalDateTime createdAt;

    private float totalAmount;

    private Boolean hasDesign;

    private Category category;

    private DesignVariantDetailRes designIdeaVariantDetail;

    private PersonalProductDetailRes personalProductDetail;
}
