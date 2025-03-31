package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailableServiceRepository extends JpaRepository<AvailableService, Long> {
    List<AvailableService> findAvailableServicesByWoodworkerProfile(WoodworkerProfile woodworkerProfile);

    AvailableService findFirstByAvailableServiceId(Long availableServiceId);

}
