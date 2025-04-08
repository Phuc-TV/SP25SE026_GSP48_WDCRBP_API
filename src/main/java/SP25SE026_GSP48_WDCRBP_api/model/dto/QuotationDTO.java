package SP25SE026_GSP48_WDCRBP_api.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDTO {

    @NotBlank(message = "Cost type is required")
    private String costType;

    @Positive(message = "Cost amount must be positive")
    private float costAmount;

    @NotBlank(message = "Quantity required is mandatory")
    private String quantityRequired;
}
