package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDepositRepository extends JpaRepository<OrderDeposit, Long> {

    Optional<OrderDeposit> findByServiceOrder_OrderId(Long orderId);
}
