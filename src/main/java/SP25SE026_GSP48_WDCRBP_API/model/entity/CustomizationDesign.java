package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CustomizationDesign")
public class CustomizationDesign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customizationDesignId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String mediaUrls;

    @Column(nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String designCode;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = false)
    private WoodworkerProfile woodworkerProfile;
}
