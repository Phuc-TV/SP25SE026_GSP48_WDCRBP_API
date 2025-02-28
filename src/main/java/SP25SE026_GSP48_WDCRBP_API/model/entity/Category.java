package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = true)
    private Long parentId;

    @Column(nullable = true)
    private String categoryName;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String categoryLevel;

    @Column(nullable = true)
    private boolean status;
}
