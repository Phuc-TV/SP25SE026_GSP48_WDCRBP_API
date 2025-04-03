package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = true)
    private String title;

    @Column(nullable = true,length = 2000)
    private String description;

    @Column(nullable = true,length = 2000)
    private String img_Urls;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "woodworkerId", nullable = true)
    private WoodworkerProfile woodworkerProfile;
}
