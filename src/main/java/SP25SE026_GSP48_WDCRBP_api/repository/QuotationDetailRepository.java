package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.QuotationDetail;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Long> {
    List<QuotationDetail> findByRequestedProduct(RequestedProduct requestedProduct);
    void deleteByRequestedProduct(RequestedProduct requestedProduct);
}
