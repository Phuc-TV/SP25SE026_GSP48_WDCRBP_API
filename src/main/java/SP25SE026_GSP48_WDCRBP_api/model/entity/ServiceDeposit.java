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
@Table(name = "ServiceDeposit")
public class ServiceDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceDepositId;

    @Column(nullable = true)
    private Short depositNumber;

    @Column(nullable = true)
    private Short percent;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Boolean status;

    @Column(nullable = true)
    private DateTime createdAt;

    @Column(nullable = true)
    private DateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = true)
    private Service service;
}
