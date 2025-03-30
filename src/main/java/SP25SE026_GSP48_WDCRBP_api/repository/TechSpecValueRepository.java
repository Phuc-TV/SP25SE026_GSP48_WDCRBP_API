package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.TechSpec;
import SP25SE026_GSP48_WDCRBP_api.model.entity.TechSpecValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechSpecValueRepository extends JpaRepository<TechSpecValue, Long> {
    List<TechSpecValue> findTechSpecValueByTechSpec(TechSpec techSpec);
}
