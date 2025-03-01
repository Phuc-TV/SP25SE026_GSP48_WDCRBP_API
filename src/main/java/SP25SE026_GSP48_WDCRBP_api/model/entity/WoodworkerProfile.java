package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WoodworkerProfile")
public class WoodworkerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long woodworkerId;

    @Column(nullable = true)
    private String brandName;

    @Column(nullable = true)
    private String bio;

    @Column(nullable = true)
    private int rating;

    @Column(nullable = true)
    private String verificationStauts;

    @Column(nullable = true)
    private String noOrder;

    @Column(nullable = true)
    private String businessType;

    @Column(nullable = true)
    private String taxCode;

    @Column(nullable = true)
    private boolean status;

    @OneToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;
}
