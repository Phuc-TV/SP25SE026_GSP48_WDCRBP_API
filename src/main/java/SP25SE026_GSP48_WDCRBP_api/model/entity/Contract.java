package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = true,length = 2000)
    private String customerSignature;

    @Column(nullable = true,length = 2000)
    private String woodworkerSignature;

    @Column(nullable = true,length = 2000)
    private String woodworkerTerms;

    @Column(nullable = true)
    private String warrantyPolicy;

    @Column(nullable = true)
    private float contractTotalAmount;

    @Column(nullable = true)
    private LocalDateTime completeDate;

    @Column(nullable = true)
    private LocalDateTime signDate;

    @Column(nullable = true)
    private String cusFullName;

    @Column(nullable = true)
    private String cusAddress;

    @Column(nullable = true)
    private String cusPhone;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;
}
