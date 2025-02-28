package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(nullable = true)
    private String serviceType;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = true)
    private WoodworkerProfile woodworkerProfile;
}
