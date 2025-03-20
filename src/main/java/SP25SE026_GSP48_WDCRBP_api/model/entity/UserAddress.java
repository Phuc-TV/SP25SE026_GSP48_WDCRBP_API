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
@Table(name = "UserAddress")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAddressId;

    @Column(nullable = true)
    private Boolean isDefault;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String wardCode;

    @Column(nullable = true)
    private String districtId;

    @Column(nullable = true)
    private String cityId;

    @Column(nullable = true)
    private DateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = true)
    private User user;
}
