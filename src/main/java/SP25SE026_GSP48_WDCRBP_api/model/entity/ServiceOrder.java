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
@Table(name = "ServiceOrder ")
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = true)
    private Byte quantity;

    @Column(nullable = true)
    private Float totalAmount;

    @Column(nullable = true)
    private boolean isInstall;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String feedback;

    @Column(nullable = true)
    private DateTime createdAt;

    @Column(nullable = true)
    private DateTime updatedAt;

    @Column(nullable = true)
    private boolean status;

    @Column(nullable = true)
    private Float amountPaid;

    @Column(nullable = true)
    private Float amountRemaining;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "availableServiceId", nullable = true)
    private AvailableService availableService;

    @OneToOne
    @JoinColumn(name = "appointmentId" ,nullable = true)
    private ConsultantAppointment consultantAppointment;
}
