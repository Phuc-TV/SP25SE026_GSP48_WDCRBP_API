package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ConsultantAppointment")
public class ConsultantAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = true)
    private LocalDateTime dateTime;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private String form;

    @Column(nullable = true,length = 1000)
    private String meetAddress;

    @Column(nullable = true,length = 1000)
    private String content;

    @Column(nullable = true)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;
}
