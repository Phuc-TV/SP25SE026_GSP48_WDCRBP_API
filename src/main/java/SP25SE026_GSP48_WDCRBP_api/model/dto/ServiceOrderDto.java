package SP25SE026_GSP48_WDCRBP_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderDto {
    private Long orderId;
    private Byte quantity;
    private Float totalAmount;
    private boolean isInstall;
    private String description;
    private String feedback;
    private Float amountPaid;
    private Float amountRemaining;
}
