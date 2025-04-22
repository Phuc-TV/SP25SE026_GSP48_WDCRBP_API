package SP25SE026_GSP48_WDCRBP_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {
    private String contractId;

    private String customerSignature;

    private String woodworkerSignature;

    private float contractTotalAmount;

    private LocalDateTime completeDate;

    private LocalDateTime signDate;

    private String cusFullName;

    private String cusAddress;

    private String cusPhone;

    private LocalDateTime createdAt;

    private LocalDateTime warrantyPeriod;
}
