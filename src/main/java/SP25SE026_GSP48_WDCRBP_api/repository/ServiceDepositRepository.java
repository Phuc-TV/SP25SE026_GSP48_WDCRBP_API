package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Service;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDepositRepository extends JpaRepository<ServiceDeposit, String> {
    List<ServiceDeposit> findServiceDepositsByService(Service service);
}
