package SP25SE026_GSP48_WDCRBP_api.repository;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ProductImages;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
    List<ProductImages> findByRequestedProduct(RequestedProduct requestedProduct);
}
