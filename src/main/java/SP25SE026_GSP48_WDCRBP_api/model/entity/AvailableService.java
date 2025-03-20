package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private DateTime createdAt;

    @Column(nullable = true)
    private DateTime updatedAt;

    @Column(nullable = true)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = true)
    private Service service;
}
