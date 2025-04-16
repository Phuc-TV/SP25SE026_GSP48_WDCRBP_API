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
@Table(name = "GuaranteeOrder")
public class GuaranteeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guaranteeOrderId;

    @Column(nullable = true)
    private Float totalAmount;

    @Column(nullable = true, length = 2000)
    private String description;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String currentStatus;

    @Column(nullable = true)
    private String currentProductImgUrls;

    @Column(nullable = true)
    private boolean status;

    @Column(nullable = true)
    private Float amountPaid;

    @Column(nullable = true)
    private Float amountRemaining;

    @ManyToOne
    @JoinColumn(name = "availableServiceId", nullable = true)
    private AvailableService availableService;

    @OneToOne
    @JoinColumn(name = "appointmentId" ,nullable = true)
    private ConsultantAppointment consultantAppointment;

    @ManyToOne
    @JoinColumn(name = "requestedProductId" ,nullable = true)
    private RequestedProduct requestedProduct;
}
