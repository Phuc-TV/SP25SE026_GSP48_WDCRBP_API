package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ConsultantAppointment;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultantAppointmentRepository extends JpaRepository<ConsultantAppointment, Long> {
}
