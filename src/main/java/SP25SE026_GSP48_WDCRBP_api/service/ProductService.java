package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ProductRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ProductRes;
import java.util.List;

public interface ProductService {
    ProductRes createProduct(ProductRequest productRequest);
    ProductRes getProductById(Long productId);
    List<ProductRes> getAllProducts();
    ProductRes updateProduct(Long productId, ProductRequest productRequest);
    void deleteProduct(Long productId);
}
