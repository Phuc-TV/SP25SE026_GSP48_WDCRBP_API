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
@Table(name = "TransactionDetail")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionDetailId;

    @Column(nullable = true)
    private float amount;

    @Column(nullable = true)
    private LocalDate transactionDateTime;

    @Column(nullable = true)
    private LocalDate dueDate;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "transactionId", nullable = true)
    private Transaction transaction;
}
