package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderProgress;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProgressRepository extends JpaRepository<OrderProgress, Long> {
    OrderProgress findOrderProgressByServiceOrder(ServiceOrder serviceOrder);
}
