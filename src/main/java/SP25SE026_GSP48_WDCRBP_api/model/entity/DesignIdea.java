package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DesignIdea")
public class DesignIdea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designIdeaId;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true,length = 2000)
    private String img_urls;

    @Column(nullable = true,length = 2000)
    private String description;

    @Column(nullable = true)
    private Short totalStar;

    @Column(nullable = true)
    private Short totalReviews;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = true)
    private WoodworkerProfile woodworkerProfile;
}
