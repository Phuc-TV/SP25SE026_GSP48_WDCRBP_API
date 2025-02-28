package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DesignFile")
public class DesignFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designFileId;

    @Column(nullable = true)
    private String mediaUrls;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private RequestedProduct requestedProduct;
}
