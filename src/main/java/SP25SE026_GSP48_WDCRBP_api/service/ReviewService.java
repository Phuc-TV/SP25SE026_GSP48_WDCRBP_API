package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;

import java.util.List;

public interface ReviewService {
    List<ReviewRes> getReviewsByWoodworkerId(Long woodworkerId);
    List<ReviewRes> getReviewsByProductId(Long productId);
    List<ReviewRes> getReviewsByDesignIdeaId(Long designId);
}
