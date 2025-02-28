package SP25SE026_GSP48_WDCRBP_API.model.entity;

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

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "requirement_form_id", nullable = false)
    private RequirementForm requirementForm;

    @Column(nullable = false)
    private Byte quantity;

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private Boolean hasDesign;

    @ManyToOne
    @JoinColumn(name = "customizationDesignId", nullable = true)
    private CustomizationDesign customizationDesign;
}
