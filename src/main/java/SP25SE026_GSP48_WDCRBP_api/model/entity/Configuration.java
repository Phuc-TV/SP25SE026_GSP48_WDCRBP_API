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
@Table(name = "Configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configurationId;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private String createdBy;

    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
