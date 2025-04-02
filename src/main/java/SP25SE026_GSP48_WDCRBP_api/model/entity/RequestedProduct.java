package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = true)
    private Byte quantity;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private float totalAmount;

    @Column(nullable = true)
    private Boolean hasDesign;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "designIdeaVariantId", nullable = true)
    private DesignIdeaVariant designIdeaVariant;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;
}
