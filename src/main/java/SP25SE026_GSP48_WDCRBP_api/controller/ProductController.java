package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ProductRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ProductRes;
import SP25SE026_GSP48_WDCRBP_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public CoreApiResponse<ProductRes> createProduct(@RequestBody ProductRequest productRequest) {
        try{
            ProductRes response = productService.createProduct(productRequest);
            return CoreApiResponse.success(response, "Tạo sản phẩm thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo sản phẩm thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/woodworker/{woodworkerId}")
    public CoreApiResponse<List<ProductRes>> getProductsByWoodworkerId(@PathVariable Long woodworkerId) {
        try {
            List<ProductRes> response = productService.getProductsByWoodworkerId(woodworkerId);
            if (response.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy sản phẩm");
            }
            return CoreApiResponse.success(response, "Lấy sản phẩm theo thợ gỗ thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi lấy sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CoreApiResponse<ProductRes> getProductById(@PathVariable Long id) {
        try{
            ProductRes response = productService.getProductById(id);
            if (response == null) {
                return CoreApiResponse.success(null, "Không tìm thấy sản phẩm");
            }
            return CoreApiResponse.success(response, "Lấy sản phẩm thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy sản phẩm thất bại: " + e.getMessage());
        }
    }

    @GetMapping
    public CoreApiResponse<List<ProductRes>> getAllProducts() {
        try{
            List<ProductRes> response = productService.getAllProducts();
            if (response == null || response.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy sản phẩm");
            }
            return CoreApiResponse.success(response, "Lấy tất cả sản phẩm thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy sản phẩm thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public CoreApiResponse<ProductRes> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try{
            ProductRes response = productService.updateProduct(id, productRequest);
            return CoreApiResponse.success(response, "Cập nhật sản phẩm thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo sản phẩm thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
