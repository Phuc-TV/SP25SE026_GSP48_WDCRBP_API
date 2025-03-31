package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/woodworker/{woodworkerId}")
    public CoreApiResponse<List<ReviewRes>> getReviewsByWoodworkerId(@PathVariable Long woodworkerId) {
        try {
            List<ReviewRes> reviews = reviewService.getReviewsByWoodworkerId(woodworkerId);
            if (reviews.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho thợ mộc này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo thợ mộc thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo thợ mộc: " + e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public CoreApiResponse<List<ReviewRes>> getReviewsByProductId(@PathVariable Long productId) {
        try {
            List<ReviewRes> reviews = reviewService.getReviewsByProductId(productId);
            if (reviews.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho sản phẩm này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo sản phẩm thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo sản phẩm: " + e.getMessage());
        }
    }

    @GetMapping("/design-variant/{designVariantIdeaId}")
    public CoreApiResponse<List<ReviewRes>> getReviewsByDesignVariantIdeaId(@PathVariable Long designVariantIdeaId) {
        try {
            List<ReviewRes> reviews = reviewService.getReviewsByDesignVariantId(designVariantIdeaId);
            if (reviews.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho ý tưởng thiết kế này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo ý tưởng thiết kế thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo ý tưởng thiết kế: " + e.getMessage());
        }
    }
}
