package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ServicePack")
public class ServicePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicePackId;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private Float price;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Short duration;
}
