package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ReviewRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateReviewStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerResponseStatusRequest;

import java.util.List;

public interface ReviewService {
    List<ReviewRes> getReviewsByWoodworkerId(Long woodworkerId);
    List<ReviewRes> getReviewsByProductId(Long productId);
    List<ReviewRes> getReviewsByDesignIdeaId(Long designId);
    ReviewRes createReview(ReviewRequest request);
    ReviewRes updateReviewStatus(Long reviewId, UpdateReviewStatusRequest updateReviewStatusRequest);
    ReviewRes updateWoodworkerResponseStatus(Long reviewId, UpdateWoodworkerResponseStatusRequest updateWoodworkerResponseStatusRequest);

}
