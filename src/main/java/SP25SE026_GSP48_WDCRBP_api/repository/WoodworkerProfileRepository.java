package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WoodworkerProfileRepository extends JpaRepository<WoodworkerProfile, Long> {
    Optional<WoodworkerProfile> findByUserEmailOrUserPhone(String email, String phone);
}
