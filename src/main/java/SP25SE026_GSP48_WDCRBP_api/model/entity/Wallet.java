package SP25SE026_GSP48_WDCRBP_api.model.entity;

import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(nullable = true)
    private Float balance;

    @Column(nullable = true)
    private DateTime createdAt;

    @Column(nullable = true)
    private DateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;
}
