package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestedProductRepository extends JpaRepository<RequestedProduct, Long> {
    List<RequestedProduct> findRequestedProductByServiceOrder(ServiceOrder serviceOrder);
    RequestedProduct findRequestedProductByRequestedProductId(Long requestedProductId);
    List<RequestedProduct> findByServiceOrder(ServiceOrder serviceOrder);
}
