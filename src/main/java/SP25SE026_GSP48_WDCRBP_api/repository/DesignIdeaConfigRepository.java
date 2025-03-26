package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DesignIdeaConfigRepository extends JpaRepository<DesignIdeaConfig,Long> {
    DesignIdeaConfig findDesignIdeaConfigBySpecifications(String specifications);
}
