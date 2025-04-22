package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Review;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findServiceOrderByUser(User user);

    List<ServiceOrder> findServiceOrderByAvailableService(AvailableService availableService);

    ServiceOrder findServiceOrderByOrderId(Long orderId);

    List<ServiceOrder> findAllByAvailableService_AvailableServiceIdIn(List<Long> availableServiceIds);

    ServiceOrder findServiceOrderByReview(Review review);
}

