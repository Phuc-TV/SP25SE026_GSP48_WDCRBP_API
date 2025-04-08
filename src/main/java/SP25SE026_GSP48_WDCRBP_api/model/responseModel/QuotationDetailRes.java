package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDetailRes {
    private RequestedProductInfo requestedProduct;
    private List<QuotationInfo> quotationDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestedProductInfo {
        private Long requestedProductId;
        private Byte quantity;
        private LocalDateTime createdAt;
        private float totalAmount;
        private Boolean hasDesign;
        private String category; // Can be Category object or just name/ID
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuotationInfo {
        private Long quotId;
        private String costType;
        private float costAmount;
        private String quantityRequired;
    }
}

