package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "Product name must not be blank")
    private String productName;
    @NotBlank(message = "Description must not be blank")
    private String description;
    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private float price;
    @Min(value = 0, message = "Stock must be at least 0")
    private short stock;
    @Min(value = 0, message = "Warranty duration must be at least 0")
    private short warrantyDuration;
    @Min(value = 0, message = "Length must be at least 0")
    private short length;
    @Min(value = 0, message = "Width must be at least 0")
    private short width;
    @Min(value = 0, message = "Height must be at least 0")
    private short height;
    @NotBlank(message = "Media URLs must not be blank")
    private String mediaUrls;
    @NotBlank(message = "Wood type must not be blank")
    private String woodType;
    @NotBlank(message = "Color must not be blank")
    private String color;
    @NotBlank(message = "Special feature must not be blank")
    private String specialFeature;
    @NotBlank(message = "Style must not be blank")
    private String style;
    @NotBlank(message = "Sculpture must not be blank")
    private String sculpture;
    @NotBlank(message = "Scent must not be blank")
    private String scent;
    @NotNull(message = "Total star must not be null")
    private Boolean status;
    @NotNull(message = "Woodworker ID is required")
    private Long woodworkerId;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private Boolean isInstall;
}
