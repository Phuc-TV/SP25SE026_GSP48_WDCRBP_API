package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.service.ReviewService;
import jakarta.validation.Valid;
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
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho xưởng mộc này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo xưởng mộc thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo xưởng mộc: " + e.getMessage());
        }
    }

    @GetMapping("/woodworker/response/{woodworkerId}")
    public CoreApiResponse<List<ReviewRes>> getReviewsNeedResponseByWoodworkerId(@PathVariable Long woodworkerId) {
        try {
            List<ReviewRes> reviews = reviewService.getReviewsNeedResponseByWoodworkerId(woodworkerId);
            if (reviews.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho xưởng mộc này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo xưởng mộc thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo xưởng mộc: " + e.getMessage());
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

    @GetMapping("/design/{designIdeaId}")
    public CoreApiResponse<List<ReviewRes>> getReviewsByDesignIdeaId(@PathVariable Long designIdeaId) {
        try {
            List<ReviewRes> reviews = reviewService.getReviewsByDesignIdeaId(designIdeaId);
            if (reviews.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy đánh giá cho ý tưởng thiết kế này.");
            }
            return CoreApiResponse.success(reviews, "Lấy đánh giá theo ý tưởng thiết kế thành công.");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá theo ý tưởng thiết kế: " + e.getMessage());
        }
    }

    @PostMapping
    public CoreApiResponse<ReviewRes> createReview(@RequestBody @Valid ReviewRequest request) {
        try {
            ReviewRes res = reviewService.createReview(request);
            return CoreApiResponse.success(res, "Tạo đánh giá thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo đánh giá thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/response")
    public CoreApiResponse<ReviewRes> createResponse(@RequestBody @Valid ReviewResponseRequest request) {
        try {
            reviewService.createReviewResponse(request.getReviewId(), request.getWoodworkerResponse());
            return CoreApiResponse.success("Tạo đánh giá thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo đánh giá thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/guarantee-order")
    public CoreApiResponse<ReviewRes> createReviewForGuaranteeOrder(@RequestBody @Valid GuaranteeReviewRequest request) {
        try {
            ReviewRes res = reviewService.createReviewForGuaranteeOrder(request);
            return CoreApiResponse.success(res, "Tạo đánh giá thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo đánh giá thất bại: " + e.getMessage());
        }
    }

    @PatchMapping("/{reviewId}/status")
    public CoreApiResponse<ReviewRes> updateReviewStatus(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewStatusRequest request) {
        try {
            ReviewRes res = reviewService.updateReviewStatus(reviewId, request);
            return CoreApiResponse.success(res, "Cập nhật trạng thái đánh giá thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Cập nhật trạng thái thất bại: " + e.getMessage());
        }
    }

    @PatchMapping("/{reviewId}/response-status")
    public CoreApiResponse<ReviewRes> updateWoodworkerResponseStatus(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateWoodworkerResponseStatusRequest request) {
        try {
            ReviewRes res = reviewService.updateWoodworkerResponseStatus(reviewId, request);
            return CoreApiResponse.success(res, "Cập nhật phản hồi thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Cập nhật phản hồi thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/status-false")
    public CoreApiResponse<List<ReviewRes>> getAllPendingReviews() {
        try {
            List<ReviewRes> reviews = reviewService.getAllPendingReviews();
            return CoreApiResponse.success(reviews, "Lấy danh sách đánh giá chưa duyệt thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá chưa duyệt: " + e.getMessage());
        }
    }

    @GetMapping("/WWResponse-status-false")
    public CoreApiResponse<List<ReviewRes>> getPendingReviewsWithWWResponse() {
        try {
            List<ReviewRes> reviews = reviewService.getPendingReviewsWithWoodworkerResponse();
            return CoreApiResponse.success(reviews, "Lấy đánh giá có phản hồi của xưởng mộc thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy đánh giá có phản hồi của xưởng mộc: " + e.getMessage());
        }
    }
}
