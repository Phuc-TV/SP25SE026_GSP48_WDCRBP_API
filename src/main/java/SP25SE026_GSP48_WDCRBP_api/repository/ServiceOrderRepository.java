package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findServiceOrderByUser(User user);

    List<ServiceOrder> findServiceOrderByAvailableService(AvailableService availableService);

    ServiceOrder findServiceOrderByOrderId(Long orderId);
}
