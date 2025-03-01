package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Integer rating;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = true)
    private String createdAt;

    @Column(nullable = true)
    private String updatedAt;
}
