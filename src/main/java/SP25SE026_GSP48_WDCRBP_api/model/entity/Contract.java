package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Contract")
public class Contract {
    @Id
    private String contractId;

    @Column(nullable = true)
    private String customerSignature;

    @Column(nullable = true)
    private String woodworkerSignature;

    @Column(nullable = true)
    private String warrantyPolicy;

    @Column(nullable = true)
    private float contractTotalAmount;

    @Column(nullable = true)
    private String completeDate;

    @Column(nullable = true)
    private String signDate;

    @Column(nullable = true)
    private String cusFullName;

    @Column(nullable = true)
    private String cusAddress;

    @Column(nullable = true)
    private String cusPhone;

    @Column(nullable = true)
    private String createdAt;

    @Column(nullable = true)
    private DateTime warrantyPeriod;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;
}
