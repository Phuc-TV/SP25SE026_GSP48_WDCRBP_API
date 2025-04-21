package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Product;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Category;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ProductRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ProductRes;
import SP25SE026_GSP48_WDCRBP_api.repository.ProductRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.CategoryRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final WoodworkerProfileRepository woodworkerProfileRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              WoodworkerProfileRepository woodworkerProfileRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductRes createProduct(ProductRequest productRequest) {
        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(productRequest.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("WoodworkerProfile not found with ID: " + productRequest.getWoodworkerId()));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productRequest.getCategoryId()));

        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setWarrantyDuration(productRequest.getWarrantyDuration());
        product.setIsInstall(productRequest.getIsInstall());
        product.setLength(productRequest.getLength());
        product.setWidth(productRequest.getWidth());
        product.setHeight(productRequest.getHeight());
        product.setMediaUrls(productRequest.getMediaUrls());
        product.setWoodType(productRequest.getWoodType());
        product.setColor(productRequest.getColor());
        product.setSpecialFeature(productRequest.getSpecialFeature());
        product.setStyle(productRequest.getStyle());
        product.setSculpture(productRequest.getSculpture());
        product.setScent(productRequest.getScent());
        product.setStatus(productRequest.getStatus());
        product.setWoodworkerProfile(woodworkerProfile);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToProductRes(savedProduct);
    }

    @Override
    public ProductRes getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::convertToProductRes)
                .orElse(null); // instead of throwing
    }

    @Override
    public List<ProductRes> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToProductRes)
                .collect(Collectors.toList());
    }

    @Override
    public ProductRes updateProduct(Long productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(productRequest.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("Woodworker not found with ID: " + productRequest.getWoodworkerId()));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productRequest.getCategoryId()));

        existingProduct.setProductName(productRequest.getProductName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setWarrantyDuration(productRequest.getWarrantyDuration());
        existingProduct.setIsInstall(productRequest.getIsInstall());
        existingProduct.setLength(productRequest.getLength());
        existingProduct.setWidth(productRequest.getWidth());
        existingProduct.setHeight(productRequest.getHeight());
        existingProduct.setMediaUrls(productRequest.getMediaUrls());
        existingProduct.setWoodType(productRequest.getWoodType());
        existingProduct.setColor(productRequest.getColor());
        existingProduct.setSpecialFeature(productRequest.getSpecialFeature());
        existingProduct.setStyle(productRequest.getStyle());
        existingProduct.setSculpture(productRequest.getSculpture());
        existingProduct.setScent(productRequest.getScent());
        existingProduct.setStatus(productRequest.getStatus());
        existingProduct.setWoodworkerProfile(woodworkerProfile);
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToProductRes(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        productRepository.delete(product);
    }

    private ProductRes convertToProductRes(Product product) {
        return ProductRes.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .warrantyDuration(product.getWarrantyDuration())
                .isInstall(product.getIsInstall())
                .length(product.getLength())
                .width(product.getWidth())
                .height(product.getHeight())
                .mediaUrls(product.getMediaUrls())
                .woodType(product.getWoodType())
                .color(product.getColor())
                .specialFeature(product.getSpecialFeature())
                .style(product.getStyle())
                .sculpture(product.getSculpture())
                .scent(product.getScent())
                .totalStar(product.getTotalStar())
                .totalReviews(product.getTotalReviews())
                .status(product.isStatus())
                .woodworkerId(product.getWoodworkerProfile().getWoodworkerId())
                .categoryId(product.getCategory().getCategoryId())
                .woodworkerName(product.getWoodworkerProfile().getBrandName())
                .categoryName(product.getCategory().getCategoryName())
                .address(product.getWoodworkerProfile().getAddress())
                .cityId(product.getWoodworkerProfile().getCityId())
                .packType(product.getWoodworkerProfile().getServicePack().getName())
                .woodworkerImgUrl(product.getWoodworkerProfile().getImgUrl())
                .servicePackEndDate(product.getWoodworkerProfile().getServicePackEndDate())
                .isWoodworkerProfilePublic(product.getWoodworkerProfile().getPublicStatus())
                .wardCode(product.getWoodworkerProfile().getWardCode())
                .districtId(product.getWoodworkerProfile().getDistrictId())
                .build();
    }

    @Override
    public List<ProductRes> getProductsByWoodworkerId(Long woodworkerId) {
        List<Product> products = productRepository.findByWoodworkerProfile_WoodworkerId(woodworkerId);
        return products.stream()
                .map(this::convertToProductRes)
                .collect(Collectors.toList());
    }

}