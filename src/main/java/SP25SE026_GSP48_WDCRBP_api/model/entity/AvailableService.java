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
@Table(name = "AvailableService")
public class AvailableService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availableServiceId;

    @Column(nullable = true,length = 1000)
    private String description;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private Boolean status;

    @Column(nullable = true)
    private Boolean operatingStatus;

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = true)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = true)
    private WoodworkerProfile woodworkerProfile;
}
