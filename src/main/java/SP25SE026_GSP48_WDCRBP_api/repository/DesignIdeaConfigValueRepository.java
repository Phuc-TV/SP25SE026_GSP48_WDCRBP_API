package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfig;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfigValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignIdeaConfigValueRepository extends JpaRepository<DesignIdeaConfigValue,Long> {
    DesignIdeaConfigValue findDesignIdeaConfigValueByDesignIdeaConfig(DesignIdeaConfig designIdeaConfig);
}
