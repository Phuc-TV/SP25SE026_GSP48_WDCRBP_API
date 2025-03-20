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

    @Column(nullable = false)
    private String costType;

    @Column(nullable = false)
    private String costAmount;

    @Column(nullable = false)
    private Short quantityRequired;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private RequestedProduct requestedProduct;
}
