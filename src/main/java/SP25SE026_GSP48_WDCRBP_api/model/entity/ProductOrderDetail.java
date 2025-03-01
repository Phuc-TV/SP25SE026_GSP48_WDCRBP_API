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
@Table(name = "ProductOrderDetail")
public class ProductOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOrderDetailId;

    @Column(nullable = true)
    private Byte quantity;

    @Column(nullable = true)
    private float unitPrice;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private CusOrder cusOrder;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = true)
    private Product product;
}
