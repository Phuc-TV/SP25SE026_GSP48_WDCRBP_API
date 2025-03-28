package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ServicePackDetails")
public class ServicePackDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicePackDetailsId;

    @Column(nullable = true)
    private Short postLimitPerMonth;

    @Column(nullable = true)
    private Boolean productManagement;

    @Column(nullable = true)
    private Short searchResultPriority;

    @Column(nullable = true)
    private Boolean personalization;

    @ManyToOne
    @JoinColumn(name = "servicePackId", nullable = true)
    private ServicePack servicePack;
}
