package SP25SE026_GSP48_WDCRBP_API.repository;

import SP25SE026_GSP48_WDCRBP_API.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
