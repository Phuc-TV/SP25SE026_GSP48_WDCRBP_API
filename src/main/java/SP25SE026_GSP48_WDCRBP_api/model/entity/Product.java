package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = true)
    private String productName;

    @Column(nullable = true,length = 1000)
    private String description;

    @Column(nullable = true)
    private float price;

    @Column(nullable = true)
    private short stock;

    @Column(nullable = true)
    private short weight;

    @Column(nullable = true)
    private short length;

    @Column(nullable = true)
    private short width;

    @Column(nullable = true)
    private short height;

    @Column(nullable = true,length = 1000)
    private String mediaUrls;

    @Column(nullable = true)
    private String woodType;

    @Column(nullable = true)
    private String color;

    @Column(nullable = true)
    private String specialFeature;

    @Column(nullable = true)
    private String style;

    @Column(nullable = true)
    private String sculpture;

    @Column(nullable = true)
    private String scent;

    @Column(nullable = true)
    private Short totalStar;

    @Column(nullable = true)
    private Short totalReviews;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = true)
    private WoodworkerProfile woodworkerProfile;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = true)
    private Category category;
}
