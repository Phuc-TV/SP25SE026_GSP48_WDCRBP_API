package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Boolean status;

    @Column(nullable = true)
    private String createdAt;

    @Column(nullable = true)
    private String updatedAt;
}
