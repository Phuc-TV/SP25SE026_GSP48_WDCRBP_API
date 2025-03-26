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
@Table(name = "DesignIdeaConfig")
public class DesignIdeaConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designIdeaConfigId;

    @Column(nullable = true)
    private String specifications;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "designIdeaId", nullable = true)
    private DesignIdea designIdea;
}
