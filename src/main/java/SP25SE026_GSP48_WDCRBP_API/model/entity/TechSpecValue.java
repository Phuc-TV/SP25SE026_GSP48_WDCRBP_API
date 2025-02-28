package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TechSpecValue")
public class TechSpecValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long techSpecValueId;

    @ManyToOne
    @JoinColumn(name = "tech_spec_id", nullable = false)
    private TechSpec techSpec;

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private String dataType;
}
