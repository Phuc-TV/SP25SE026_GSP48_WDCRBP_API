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
    private String productCurrentStatus;

    @Column(nullable = true, length = 2000)
    private String currentProductImgUrls;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private Float shipFee;

    @Column(nullable = true)
    private boolean isInstall;

    @Column(nullable = true)
    private Boolean isGuarantee;

    @Column(nullable = true)
    private String guaranteeError;

    @Column(nullable = true)
    private Float amountPaid;

    @Column(nullable = true)
    private Float amountRemaining;

    @Column(nullable = true)
    private String role;

    @Column(nullable = true)
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "availableServiceId", nullable = true)
    private AvailableService availableService;

    @OneToOne
    @JoinColumn(name = "appointmentId" ,nullable = true)
    private ConsultantAppointment consultantAppointment;

    @OneToOne
    @JoinColumn(name = "reviewId" ,nullable = true)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "requestedProductId" ,nullable = true)
    private RequestedProduct requestedProduct;
}
