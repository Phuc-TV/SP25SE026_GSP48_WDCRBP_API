package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.QuotationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuaranteeQuotationDetailRequest {

    @NotNull(message = "guaranteeOrderId is required")
    private Long guaranteeOrderId;

    @NotEmpty(message = "At least one quotation is required")
    private List<@Valid QuotationDTO> quotations; // yes, typo matches FE, or fix it
}
