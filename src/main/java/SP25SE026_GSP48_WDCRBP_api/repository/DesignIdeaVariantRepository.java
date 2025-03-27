package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignIdeaVariantRepository extends JpaRepository<DesignIdeaVariant,Long> {
    List<DesignIdeaVariant> findDesignIdeaVariantByDesignIdea(DesignIdea designIdea);

    DesignIdeaVariant findDesignIdeaVariantByDesignIdeaVariantId(Long designIdeaVariantId);
}
