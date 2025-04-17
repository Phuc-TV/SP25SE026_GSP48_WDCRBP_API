package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QuotationDetail")
public class QuotationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotationDetailId;

    @Column(nullable = true)
    private String costType;

    @Column(nullable = true)
    private float costAmount;

    @Column(nullable = true)
    private String quantityRequired;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = true)
    private RequestedProduct requestedProduct;

    @ManyToOne
    @JoinColumn(name = "guarantee_order_id", nullable = true)
    private GuaranteeOrder guaranteeOrder;
}
