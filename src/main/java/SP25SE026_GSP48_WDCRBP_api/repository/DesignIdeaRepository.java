package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignIdeaRepository extends JpaRepository<DesignIdea, Long> {
    List<DesignIdea> findDesignIdeaByWoodworkerProfile(WoodworkerProfile woodworkerProfile);

    DesignIdea findDesignIdeaByName(String name);

    DesignIdea findDesignIdeaByDesignIdeaId(Long id);
}
