package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String updatedAt;
}
