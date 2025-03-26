package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = true)
    private String imgUrl;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime servicePackStartDate;

    @Column(nullable = true)
    private LocalDateTime servicePackEndDate;

    @Column(nullable = true)
    private Short totalStar;

    @Column(nullable = true)
    private Short totalReviews;

    @OneToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "servicePackId", nullable = true)
    private ServicePack servicePack;
}
