package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ProductImages")
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImagesId;

    @Column(nullable = true,length = 2000)
    private String mediaUrls;

    @Column(nullable = true)
    private String type;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private RequestedProduct requestedProduct;
}
