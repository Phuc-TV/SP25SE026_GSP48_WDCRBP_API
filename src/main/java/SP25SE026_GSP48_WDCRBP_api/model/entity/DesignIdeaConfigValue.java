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
@Table(name = "DesignIdeaConfigValue")
public class DesignIdeaConfigValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designIdeaConfigValueId;

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private DateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "designIdeaConfigId", nullable = true)
    private DesignIdeaConfig designIdeaConfig;
}
