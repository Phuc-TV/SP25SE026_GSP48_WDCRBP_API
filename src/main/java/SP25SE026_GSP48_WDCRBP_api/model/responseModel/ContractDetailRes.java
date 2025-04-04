package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractDetailRes {
    private Long contractId;

    private String customerSignature;

    private String woodworkerSignature;

    private String warrantyPolicy;

    private float contractTotalAmount;

    private LocalDateTime completeDate;

    private LocalDateTime signDate;

    private String cusFullName;

    private String cusAddress;

    private String cusPhone;

    private LocalDateTime createdAt;

    private LocalDateTime warrantyPeriod;

    private UserDetailRes woodworker;

    private UserDetailRes customer;
}
