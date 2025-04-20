package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GuaranteeReviewRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ReviewRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateReviewStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateWoodworkerResponseStatusRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.GuaranteeOrderService;
import SP25SE026_GSP48_WDCRBP_api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final GuaranteeOrderRepository guaranteeOrderRepository;

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
        List<GuaranteeOrder> guaranteeOrders = guaranteeOrderRepository.findGuaranteeOrderByAvailableService_WoodworkerProfile(woodworker);

        // Step 3: Extract and map non-null reviews from both ServiceOrder and GuaranteeOrder
        List<ReviewRes> serviceOrderReviews = orders.stream()
                .map(order -> {
                    Review review = order.getReview();
                    if (review != null && review.getStatus() != null) {
                        String serviceName = order.getAvailableService().getService().getServiceName();
                        return toReviewRes(review, serviceName);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<ReviewRes> guaranteeOrderReviews = guaranteeOrders.stream()
                .map(order -> {
                    Review review = order.getReview();
                    if (review != null && review.getStatus() != null) {
                        String serviceName = order.getAvailableService().getService().getServiceName();
                        return toReviewRes(review, serviceName);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Step 4: Combine both lists
        List<ReviewRes> allReviews = new ArrayList<>();
        allReviews.addAll(serviceOrderReviews);
        allReviews.addAll(guaranteeOrderReviews);

        return allReviews;
    }

    @Override
    public List<ReviewRes> getReviewsNeedResponseByWoodworkerId(Long woodworkerId) {
        WoodworkerProfile woodworker = woodworkerProfileRepository.findWoodworkerProfileByWoodworkerId(woodworkerId);
        if (woodworker == null) return new ArrayList<>();

        // Step 1: Get all available services by woodworker
        List<AvailableService> services = availableServiceRepository.findAvailableServicesByWoodworkerProfile(woodworker);

        // Step 2: Get all service orders tied to those services
        List<Long> serviceIds = services.stream()
                .map(AvailableService::getAvailableServiceId)
                .toList();

        List<ServiceOrder> orders = serviceOrderRepository.findAllByAvailableService_AvailableServiceIdIn(serviceIds);
        List<GuaranteeOrder> guaranteeOrders = guaranteeOrderRepository.findGuaranteeOrderByAvailableService_WoodworkerProfile(woodworker);

        // Step 3: Lấy review chưa được phản hồi từ ServiceOrder
        List<ReviewRes> serviceOrderReviews = orders.stream()
                .map(order -> {
                    Review review = order.getReview();
                    if (review != null && review.getStatus() != null && review.getWoodworkerResponse() == null) {
                        String serviceName = order.getAvailableService().getService().getServiceName();
                        return toReviewRes(review, serviceName);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Step 4: Lấy review chưa được phản hồi từ GuaranteeOrder
        List<ReviewRes> guaranteeOrderReviews = guaranteeOrders.stream()
                .map(order -> {
                    Review review = order.getReview();
                    if (review != null && review.getStatus() != null && review.getWoodworkerResponse() == null) {
                        String serviceName = order.getAvailableService().getService().getServiceName();
                        return toReviewRes(review, serviceName);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Step 5: Gộp cả hai list lại
        List<ReviewRes> allReviews = new ArrayList<>();
        allReviews.addAll(serviceOrderReviews);
        allReviews.addAll(guaranteeOrderReviews);

        return allReviews;
    }

    @Override
    public List<ReviewRes> getReviewsByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return new ArrayList<>();

        List<ServiceOrder> orders = serviceOrderRepository.findServiceOrdersByProductId(productId);

        return orders.stream()
                .map(order -> toReviewRes(order.getReview(), product.getProductName()))
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
                .filter(review -> review != null && review.getStatus() != null)
                .map(review -> toReviewRes(review,""))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewRes getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return toReviewRes(review, "");
    }

    @Override
    public void createReviewResponse(Long reviewId, String woodworkerResponse) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setWoodworkerResponse(woodworkerResponse);
        review.setWoodworkerResponseStatus(false);
        review.setResponseAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    private ReviewRes toReviewRes(Review review, String serviceName) {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByReview(review);
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByReview(review);

        return ReviewRes.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .username(review.getUser().getUsername())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .woodworkerResponse(review.getWoodworkerResponse())
                .woodworkerResponseStatus(review.getWoodworkerResponseStatus())
                .responseAt(review.getResponseAt())
                .serviceName(serviceName)
                .serviceOrderId(serviceOrder != null ? serviceOrder.getOrderId() : null)
                .guaranteeOrderId(guaranteeOrder != null ? guaranteeOrder.getGuaranteeOrderId() : null)
                .build();
    }

    @Override
    public ReviewRes createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(request.getServiceOrderId());

        Review review = Review.builder()
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(false)
                .woodworkerResponseStatus(false)
                .responseAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);

        serviceOrder.setReview(saved);
        serviceOrderRepository.save(serviceOrder);

        return toReviewRes(saved, "");
    }

    @Override
    public ReviewRes createReviewForGuaranteeOrder(GuaranteeReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(request.getGuaranteeOrderId());

        Review review = Review.builder()
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(false)
                .woodworkerResponseStatus(false)
                .responseAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);

        guaranteeOrder.setReview(saved);
        guaranteeOrderRepository.save(guaranteeOrder);

        return toReviewRes(saved, "");
    }

    private void updateTotalStarForServiceOrder(ServiceOrder serviceOrder, Review review) {
        WoodworkerProfile woodworker = serviceOrder.getAvailableService().getWoodworkerProfile();

        woodworker.setTotalStar((short) (
                Optional.ofNullable(woodworker.getTotalStar()).orElse((short) 0) + review.getRating()
        ));
        woodworker.setTotalReviews((short) (
                Optional.ofNullable(woodworker.getTotalReviews()).orElse((short) 0) + 1
        ));
        woodworkerProfileRepository.save(woodworker);

        if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.CUSTOMIZATION)) {
            List<RequestedProduct> requestedProducts = serviceOrder.getRequestedProducts();

            for (RequestedProduct requestedProduct : requestedProducts) {
                DesignIdea designIdea = requestedProduct.getDesignIdeaVariant().getDesignIdea();
                designIdea.setTotalStar((short) (
                        Optional.ofNullable(designIdea.getTotalStar()).orElse((short) 0) + review.getRating()
                ));
                designIdea.setTotalReviews((short) (
                        Optional.ofNullable(designIdea.getTotalReviews()).orElse((short) 0) + 1
                ));
                designIdeaRepository.save(designIdea);
            }
        } else if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.SALE)) {

        }
    }

    private void updateTotalStarForGuaranteeOrder(GuaranteeOrder guaranteeOrder, Review review) {
        WoodworkerProfile woodworker = guaranteeOrder.getAvailableService().getWoodworkerProfile();

        woodworker.setTotalStar((short) (
                Optional.ofNullable(woodworker.getTotalStar()).orElse((short) 0) + review.getRating()
        ));
        woodworker.setTotalReviews((short) (
                Optional.ofNullable(woodworker.getTotalReviews()).orElse((short) 0) + 1
        ));
        woodworkerProfileRepository.save(woodworker);
    }

    @Override
    public ReviewRes updateReviewStatus(Long reviewId, UpdateReviewStatusRequest dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!dto.getStatus()) {
            review.setStatus(null);
        } else {
            review.setStatus(true);
        }
        review.setUpdatedAt(LocalDateTime.now());

        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByReview(review);
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByReview(review);
        if (serviceOrder != null && review.getStatus() != null && review.getStatus()) {
            updateTotalStarForServiceOrder(serviceOrder, review);
        }
        if (guaranteeOrder != null && review.getStatus() != null && review.getStatus()) {
            updateTotalStarForGuaranteeOrder(guaranteeOrder, review);
        }

        return toReviewRes(reviewRepository.save(review), "");
    }

    @Override
    public ReviewRes updateWoodworkerResponseStatus(Long reviewId, UpdateWoodworkerResponseStatusRequest dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!dto.getWoodworkerResponseStatus()) {
            review.setWoodworkerResponseStatus(null);
        } else {
            review.setWoodworkerResponseStatus(true);
        }
        review.setResponseAt(LocalDateTime.now());
        return toReviewRes(reviewRepository.save(review), "");
    }

    @Override
    public List<ReviewRes> getAllPendingReviews() {
        List<Review> reviews = reviewRepository.findAllByStatusFalse();
        return reviews.stream()
                .map(review -> toReviewRes(review, ""))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewRes> getPendingReviewsWithWoodworkerResponse() {
        List<Review> reviews = reviewRepository.findAllByStatusFalseAndWoodworkerResponseIsNotNull();
        return reviews.stream()
                .map(review -> toReviewRes(review, ""))
                .collect(Collectors.toList());
    }
}
