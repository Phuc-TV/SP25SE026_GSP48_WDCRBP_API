package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    List<Configuration> findByDescriptionContainingIgnoreCaseOrValueContainingIgnoreCase(String description, String value);
}
