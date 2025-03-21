package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = true)
    private String transactionType;

    @Column(nullable = true)
    private float amount;

    @Column(nullable = true)
    private LocalDate createdAt;

    @Column(nullable = true)
    private LocalDate canceledAt;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "orderDepositId", nullable = true)
    private OrderDeposit orderDeposit;

    @ManyToOne
    @JoinColumn(name = "paymentId", nullable = true)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "walletId", nullable = true)
    private Wallet wallet;
}
