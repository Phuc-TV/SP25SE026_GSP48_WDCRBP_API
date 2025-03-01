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
@Table(name = "RequirementForm")
public class RequirementForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementFormId;

    @Column(nullable = true)
    private Byte versionNo;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private float maximumAmount;

    @Column(nullable = true)
    private LocalDateTime estimatedCompleteDate;

    @Column(nullable = true)
    private String note;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private CusOrder cusOrder;
}
