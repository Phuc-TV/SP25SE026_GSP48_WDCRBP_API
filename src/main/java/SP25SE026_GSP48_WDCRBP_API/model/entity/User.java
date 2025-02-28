package SP25SE026_GSP48_WDCRBP_API.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String role;

    @Column(nullable = true)
    private float balance;

    @Column(nullable = true)
    private String wardCode;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String bankId;

    @Column(nullable = true)
    private String bankNumber;

    @Column(nullable = true)
    private int districtId;

    @Column(nullable = true)
    private int cityId;

    @Column(nullable = true)
    private boolean status;
}
