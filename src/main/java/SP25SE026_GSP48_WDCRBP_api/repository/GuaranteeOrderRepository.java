package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuaranteeOrderRepository extends JpaRepository<GuaranteeOrder, Long> {

    GuaranteeOrder findGuaranteeOrderByGuaranteeOrderId(Long guaranteeOrderId);

    List<GuaranteeOrder> findByUser(User user);

    List<GuaranteeOrder> findByAvailableService(AvailableService availableService);

    GuaranteeOrder findGuaranteeOrderByReview(Review review);

    List<GuaranteeOrder> findGuaranteeOrderByAvailableService_WoodworkerProfile(WoodworkerProfile availableServiceWoodworkerProfile);
}