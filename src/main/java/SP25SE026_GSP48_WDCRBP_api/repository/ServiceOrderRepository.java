package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.AvailableService;
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

    @Query("""
        SELECT so FROM ServiceOrder so
        JOIN so.review r
        JOIN so.availableService avs
        JOIN avs.woodworkerProfile wp
        JOIN Product p ON p.woodworkerProfile = wp
        WHERE p.productId = :productId 
        AND r.status = true
    """)
    List<ServiceOrder> findServiceOrdersByProductId(@Param("productId") Long productId);
}

