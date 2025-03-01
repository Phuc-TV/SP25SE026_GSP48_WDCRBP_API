package SP25SE026_GSP48_WDCRBP_api.model.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Column(nullable = true)
    private Boolean isSignByA;

    @Column(nullable = true)
    private Boolean isSignByB;

    @Column(nullable = true)
    private String quote;

    @Column(nullable = true)
    private String warrantyPolicy;

    @Column(nullable = true)
    private float contractTotalAmount;

    @Column(nullable = true)
    private String completeDate;

    @Column(nullable = true)
    private String signDate;

    @Column(nullable = true)
    private String aInformation;

    @Column(nullable = true)
    private String bInformation;

    @Column(nullable = true)
    private String createdAt;

    @Column(nullable = true)
    private String contractNumber;

    @Column(nullable = true)
    private float platformCommission;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private CusOrder cusOrder;
}
