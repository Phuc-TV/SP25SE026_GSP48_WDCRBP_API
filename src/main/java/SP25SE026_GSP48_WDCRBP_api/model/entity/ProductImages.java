package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DesignFile")
public class ProductImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designFileId;

    @Column(nullable = true,length = 1000)
    private String mediaUrls;

    @Column(nullable = true)
    private Byte versionNo;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private RequestedProduct requestedProduct;
}
