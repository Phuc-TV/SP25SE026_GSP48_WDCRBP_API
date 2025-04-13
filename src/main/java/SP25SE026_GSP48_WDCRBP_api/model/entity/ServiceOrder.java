package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ServiceOrder ")
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = true)
    private Short quantity;

    @Column(nullable = true)
    private Float totalAmount;

    @Column(nullable = true)
    private Float shipFee;

    @Column(nullable = true)
    private boolean isInstall;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String feedback;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private Float amountPaid;

    @Column(nullable = true)
    private Float amountRemaining;

    @Column(nullable = true)
    private String role;

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

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestedProduct> requestedProducts = new ArrayList<>();
}
