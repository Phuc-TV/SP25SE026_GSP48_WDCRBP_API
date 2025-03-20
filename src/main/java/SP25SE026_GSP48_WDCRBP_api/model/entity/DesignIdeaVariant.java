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
@Table(name = "DesignIdeaVariant")
public class DesignIdeaVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designIdeaVariantId;

    @Column(nullable = true)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "designIdeaId", nullable = true)
    private DesignIdea decideDesignIdea;
}
