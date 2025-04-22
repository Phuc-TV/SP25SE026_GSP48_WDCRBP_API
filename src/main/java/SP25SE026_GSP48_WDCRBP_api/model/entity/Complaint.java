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
@Table(name = "Complaint")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintId;

    @Column(nullable = true)
    private String complaintType;

    @Column(nullable = true,length = 500)
    private String description;

    @Column(nullable = true,length = 500)
    private String woodworkerResponse;

    @Column(nullable = true,length = 500)
    private String staffResponse;

    @Column(nullable = true,length = 2000)
    private String proofImgUrls;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private float refundAmount;

    @OneToOne
    @JoinColumn(name = "refund_credit_transaction_id", nullable = true)
    private Transaction refundCreditTransaction;

    @OneToOne
    @JoinColumn(name = "refund_debit_transaction_id", nullable = true)
    private Transaction refundDebitTransaction;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "staff_user_id", nullable = true)
    private User staffUser;
}
