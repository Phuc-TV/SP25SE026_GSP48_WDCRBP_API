package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ReviewRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateReviewStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerResponseStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final WoodworkerProfileRepository woodworkerProfileRepository;
    private final AvailableServiceRepository availableServiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ProductRepository productRepository;
    private final DesignIdeaRepository designIdeaRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReviewRes> getReviewsByWoodworkerId(Long woodworkerId) {
        WoodworkerProfile woodworker = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(woodworkerId);
        if (woodworker == null) return new ArrayList<>();

        // Step 1: Get all available services by woodworker
        List<AvailableService> services = availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworker);

        // Step 2: Get all service orders tied to those services
        List<Long> serviceIds = services.stream()
                .map(AvailableService::getAvailableServiceId)
                .toList();

        List<ServiceOrder> orders = serviceOrderRepository.findAllByAvailableService_AvailableServiceIdIn(serviceIds);

        // Step 3: Extract and map non-null reviews
        return orders.stream()
                .map(order -> {
                    Review review = order.getReview();
                    if (review != null && review.isStatus()) {
                        String serviceName = order.getAvailableService().getService().getServiceName();
                        return toReviewRes(review, serviceName);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewRes> getReviewsByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return new ArrayList<>();

        List<ServiceOrder> orders = serviceOrderRepository.findAll();

        return orders.stream()
                .filter(order -> order.getAvailableService() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile().getWoodworkerId().equals(
                        product.getWoodworkerProfile().getWoodworkerId()
                ))
                .map(ServiceOrder::getReview)
                .filter(review -> review != null && review.isStatus())
                .map(review -> toReviewRes(review,""))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewRes> getReviewsByDesignIdeaId(Long designId) {
        DesignIdea designIdea = designIdeaRepository.findDesignIdeaByDesignIdeaId(designId);
        if (designIdea == null) return new ArrayList<>();

        List<ServiceOrder> orders = serviceOrderRepository.findAll();

        return orders.stream()
                .filter(order -> order.getAvailableService() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile().getWoodworkerId().equals(
                        designIdea.getWoodworkerProfile().getWoodworkerId()
                ))
                .map(ServiceOrder::getReview)
                .filter(review -> review != null && review.isStatus())
                .map(review -> toReviewRes(review,""))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewRes getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return toReviewRes(review, "");
    }

    private ReviewRes toReviewRes(Review review, String serviceName) {
        return ReviewRes.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .username(review.getUser().getUsername())
                .description(review.getDescription())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .woodworkerResponse(review.getWoodworkerResponse())
                .woodworkerResponseStatus(review.isWoodworkerResponseStatus())
                .responseAt(review.getResponseAt())
                .serviceName(serviceName)
                .build();
    }

    @Override
    public ReviewRes createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = Review.builder()
                .user(user)
                .description(request.getDescription())
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .woodworkerResponse(request.getWoodworkerResponse())
                .status(true)
                .woodworkerResponseStatus(false)
                .responseAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        return toReviewRes(saved, "");
    }

    @Override
    public ReviewRes updateReviewStatus(Long reviewId, UpdateReviewStatusRequest dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus(dto.getStatus());
        review.setUpdatedAt(LocalDateTime.now());
        return toReviewRes(reviewRepository.save(review), "");
    }

    @Override
    public ReviewRes updateWoodworkerResponseStatus(Long reviewId, UpdateWoodworkerResponseStatusRequest dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setWoodworkerResponseStatus(dto.getWoodworkerResponseStatus());
        review.setResponseAt(LocalDateTime.now());
        return toReviewRes(reviewRepository.save(review), "");
    }

}
