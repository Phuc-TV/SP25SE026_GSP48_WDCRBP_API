package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.CustomerSelection;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSelectionRepository extends JpaRepository<CustomerSelection, Long> {
    List<CustomerSelection> findByRequestedProduct(RequestedProduct requestedProduct);
}
