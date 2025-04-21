package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String paymentFor;

    @Column(nullable = true)
    private float amount;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "orderDepositId", nullable = true)
    private OrderDeposit orderDeposit;

    @ManyToOne
    @JoinColumn(name = "walletId", nullable = true)
    private Wallet wallet;
}
