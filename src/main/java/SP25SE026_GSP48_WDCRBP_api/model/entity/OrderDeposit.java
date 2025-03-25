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
@Table(name = "OrderDeposit")
public class OrderDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDepositId;

    @Column(nullable = true)
    private Float amount;

    @Column(nullable = true)
    private Short percent;

    @Column(nullable = true)
    private Short depositNumber;

    @Column(nullable = true)
    private Boolean status;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "orderId" ,nullable = true)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "guranteeOrderId" ,nullable = true)
    private GuranteeOrder guranteeOrder;
}
