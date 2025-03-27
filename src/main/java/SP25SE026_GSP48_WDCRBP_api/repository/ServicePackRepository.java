package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePackRepository extends JpaRepository<ServicePack, Long> {
    ServicePack findServicePackByServicePackId(Long servicePackId);
}
