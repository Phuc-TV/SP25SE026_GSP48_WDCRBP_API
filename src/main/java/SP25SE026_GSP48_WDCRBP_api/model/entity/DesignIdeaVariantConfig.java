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
@Table(name = "DesignIdeaVariantConfig")
public class DesignIdeaVariantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designIdeaVariantConfigId;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "designIdeaVariantId", nullable = true)
    private DesignIdeaVariant designIdeaVariant;

    @ManyToOne
    @JoinColumn(name = "designIdeaConfigValueId", nullable = true)
    private DesignIdeaConfigValue designIdeaConfigValue;
}
