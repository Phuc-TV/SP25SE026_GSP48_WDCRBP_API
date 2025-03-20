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
@Table(name = "PaymentMethod")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = true)
    private String methodType;

    @Column(nullable = true)
    private String providerName;

    @Column(nullable = true)
    private String accountNumber;

    @Column(nullable = true)
    private Boolean isDefault;

    @Column(nullable = true)
    private DateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;
}
