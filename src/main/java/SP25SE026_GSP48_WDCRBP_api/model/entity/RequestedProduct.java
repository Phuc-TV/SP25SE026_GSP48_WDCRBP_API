package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RequestedProduct")
public class RequestedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestedProductId;

    @Column(nullable = false)
    private Byte quantity;

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private Boolean hasDesign;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;


    @ManyToOne
    @JoinColumn(name = "designIdeaVariantId", nullable = false)
    private DesignIdeaVariant designIdeaVariant;
}
