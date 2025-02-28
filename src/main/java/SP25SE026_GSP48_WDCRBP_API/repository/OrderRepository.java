package SP25SE026_GSP48_WDCRBP_API.repository;

import SP25SE026_GSP48_WDCRBP_API.model.entity.CusOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CusOrder, Long> {
}
