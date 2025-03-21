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
@Table(name = "Service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(nullable = true)
    private String serviceName;

    @Column(nullable = true)
    private boolean status;

    @Column(nullable = true)
    private DateTime createdAt;

    @Column(nullable = true)
    private DateTime updatedAt;

    @Column(nullable = true)
    private Short numberOfDeposits;
}
