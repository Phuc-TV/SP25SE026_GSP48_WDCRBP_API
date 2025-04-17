package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuaranteeOrderIdRequest {
    @NotNull(message = "guaranteeOrderId is required")
    private Long guaranteeOrderId;
}
