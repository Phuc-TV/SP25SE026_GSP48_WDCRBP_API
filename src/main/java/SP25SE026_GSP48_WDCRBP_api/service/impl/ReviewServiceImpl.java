package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ReviewRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final WoodworkerProfileRepository woodworkerProfileRepository;
    private final AvailableServiceRepository availableServiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ProductRepository productRepository;
    private final DesignIdeaVariantRepository designIdeaVariantRepository;

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
                .map(ServiceOrder::getReview)
                .filter(review -> review != null)
                .map(this::toReviewRes)
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
                .filter(review -> review != null)
                .map(this::toReviewRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewRes> getReviewsByDesignVariantId(Long designVariantId) {
        DesignIdeaVariant variant = designIdeaVariantRepository.findDesignIdeaVariantByDesignIdeaVariantId(designVariantId);
        if (variant == null || variant.getDesignIdea() == null) return new ArrayList<>();

        DesignIdea designIdea = variant.getDesignIdea();
        List<ServiceOrder> orders = serviceOrderRepository.findAll();

        return orders.stream()
                .filter(order -> order.getAvailableService() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile() != null)
                .filter(order -> order.getAvailableService().getWoodworkerProfile().getWoodworkerId().equals(
                        designIdea.getWoodworkerProfile().getWoodworkerId()
                ))
                .map(ServiceOrder::getReview)
                .filter(review -> review != null)
                .map(this::toReviewRes)
                .collect(Collectors.toList());
    }

    private ReviewRes toReviewRes(Review review) {
        return ReviewRes.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .username(review.getUser().getUsername()) // or getUsername(), depends on your entity
                .description(review.getDescription())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .woodworkerResponse(review.getWoodworkerResponse())
                .woodworkerResponseStatus(review.isWookworkerResponseStatus())
                .responseAt(review.getResponseAt())
                .build();
    }
}
