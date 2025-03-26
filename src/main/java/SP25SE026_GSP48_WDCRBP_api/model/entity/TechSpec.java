package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TechSpec")
public class TechSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long techSpecId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String optionType;

    @Column(nullable = true)
    private boolean status;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
