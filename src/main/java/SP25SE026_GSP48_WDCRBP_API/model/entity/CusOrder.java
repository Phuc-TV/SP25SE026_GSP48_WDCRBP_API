package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CusOrder ")
public class CusOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = true)
    private String orderType;

    @Column(nullable = true)
    private Byte quantity;

    @Column(nullable = true)
    private Float totalAmount;

    @Column(nullable = true)
    private Byte shipmentId;

    @Column(nullable = true)
    private boolean isInstall;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "transactionId", nullable = true)
    private Transaction transaction;

    @OneToOne
    @JoinColumn(name = "serviceId", nullable = true)
    private Service service;
}
