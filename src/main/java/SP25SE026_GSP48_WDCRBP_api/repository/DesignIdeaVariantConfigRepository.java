package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignIdeaVariantConfigRepository extends JpaRepository<DesignIdeaVariantConfig,Long> {
    List<DesignIdeaVariantConfig> findDesignIdeaVariantConfigByDesignIdeaVariant(DesignIdeaVariant designIdeaVariant);
}
