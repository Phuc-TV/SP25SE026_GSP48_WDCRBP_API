package SP25SE026_GSP48_WDCRBP_API.repository;

import SP25SE026_GSP48_WDCRBP_API.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrPhone(String email, String phone);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
