package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListItemRes {
    private Long productId;

    private String productName;

    private String description;

    private float price;

    private short stock;

    private short warrantyDuration;

    private Boolean isInstall;

    private short length;

    private short width;

    private short height;

    private String mediaUrls;

    private String woodType;

    private String color;

    private String specialFeature;

    private String style;

    private String sculpture;

    private String scent;

    private Short totalStar;

    private Short totalReviews;

    private boolean status;
}