package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.GuaranteeOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDepositRepository extends JpaRepository<OrderDeposit, Long> {
    List<OrderDeposit> findOrderDepositByServiceOrder(ServiceOrder serviceOrder);

    List<OrderDeposit> findOrderDepositsByGuaranteeOrder(GuaranteeOrder guaranteeOrder);
}
