package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.TechSpec;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechSpecRepository extends JpaRepository<TechSpec, Long> {
    TechSpec findTechSpecByTechSpecId(Long techSpecId);
}
