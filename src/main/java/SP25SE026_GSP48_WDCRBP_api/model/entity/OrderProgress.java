package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Progress")

public class OrderProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Long createdBy;

    @Column(nullable = true)
    private Long deletedBy;

    @Column(nullable = true)
    private LocalDateTime createdTime;

    @Column(nullable = true)
    private LocalDateTime deletedTime;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "guranteeOrderId", nullable = true)
    private GuaranteeOrder guaranteeOrder;
}
