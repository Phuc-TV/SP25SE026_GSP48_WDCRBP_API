package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.GuaranteeOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findShipmentByServiceOrder(ServiceOrder serviceOrder);

    List<Shipment> findShipmentByGuaranteeOrder(GuaranteeOrder guaranteeOrder);
}
