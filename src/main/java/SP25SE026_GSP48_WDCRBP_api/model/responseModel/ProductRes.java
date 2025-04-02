package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRes {
    private Long productId;
    private String productName;
    private String description;
    private float price;
    private short stock;
    private short weight;
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
    private Long woodworkerId;
    private Long categoryId;
    private String woodworkerName;
    private String categoryName;
    private String address;
    private String cityId;
    private String packType;
    private String woodworkerImgUrl;
    private LocalDateTime servicePackEndDate;
}