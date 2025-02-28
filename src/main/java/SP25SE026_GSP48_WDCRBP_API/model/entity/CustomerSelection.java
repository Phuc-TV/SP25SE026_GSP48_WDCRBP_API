package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CustomerSelection")
public class CustomerSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerSelectionId;

    @ManyToOne
    @JoinColumn(name = "requested_product_id", nullable = false)
    private RequestedProduct requestedProduct;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private String dataType;

    @ManyToOne
    @JoinColumn(name = "techSpecId", nullable = false)
    private TechSpec techSpec;

    @Column(nullable = true)
    private LocalDate createdAt;
}
