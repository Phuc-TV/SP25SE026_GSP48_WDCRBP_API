package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Post;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PostRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PostRes;
import SP25SE026_GSP48_WDCRBP_api.repository.PostRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.WoodworkerProfileRepository;
import SP25SE026_GSP48_WDCRBP_api.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final WoodworkerProfileRepository woodworkerProfileRepository;

    public PostServiceImpl(PostRepository postRepository, WoodworkerProfileRepository woodworkerProfileRepository) {
        this.postRepository = postRepository;
        this.woodworkerProfileRepository = woodworkerProfileRepository;
    }

    @Override
    public PostRes createPost(PostRequest postRequest) {
        // 1. Get woodworker profile
        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(postRequest.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("WoodworkerProfile not found with ID: " + postRequest.getWoodworkerId()));

        // 2. Get post limit from service pack
        if (woodworkerProfile.getServicePack() == null) {
            throw new RuntimeException("Woodworker does not have a service pack assigned.");
        }

        Short postLimitPerMonth = woodworkerProfile.getServicePack().getPostLimitPerMonth();
        if (postLimitPerMonth == null) {
            throw new RuntimeException("Service pack does not define a post limit.");
        }

        // 3. Count how many posts this woodworker created this month
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int currentMonthPostCount = postRepository
                .findByWoodworkerProfile_WoodworkerId(postRequest.getWoodworkerId()).stream()
                .filter(post -> post.getCreatedAt() != null && post.getCreatedAt().isAfter(startOfMonth))
                .toList()
                .size();

        // 4. Check if the limit is exceeded
        if (currentMonthPostCount >= postLimitPerMonth) {
            throw new RuntimeException("Đã vượt quá số lượng bài viết cho phép trong tháng này.");
        }

        // 5. Create post if allowed
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setImg_Urls(postRequest.getImgUrls());
        post.setCreatedAt(LocalDateTime.now());
        post.setWoodworkerProfile(woodworkerProfile);

        Post savedPost = postRepository.save(post);
        return convertToPostRes(savedPost);
    }

//    @Override
//    public PostRes createPost(PostRequest postRequest) {
//        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(postRequest.getWoodworkerId())
//                .orElseThrow(() -> new RuntimeException("WoodworkerProfile not found with ID: " + postRequest.getWoodworkerId()));
//
//        // Ensure service pack and limits exist
//        if (woodworkerProfile.getServicePack() == null || woodworkerProfile.getServicePackStartDate() == null) {
//            throw new RuntimeException("Woodworker does not have a valid service pack or start date.");
//        }
//
//        Short postLimit = woodworkerProfile.getServicePack().getPostLimitPerMonth();
//        if (postLimit == null) {
//            throw new RuntimeException("Service pack does not define a post limit.");
//        }
//
//        // Count posts AFTER the servicePackStartDate (ignore previous plan's posts)
//        LocalDateTime effectiveStart = woodworkerProfile.getServicePackStartDate();
//        int postsSinceUpgrade = postRepository.findByWoodworkerProfile_WoodworkerId(postRequest.getWoodworkerId()).stream()
//                .filter(post -> post.getCreatedAt() != null && post.getCreatedAt().isAfter(effectiveStart))
//                .toList().size();
//
//        if (postsSinceUpgrade >= postLimit) {
//            throw new RuntimeException("Bạn đã đăng hết số lượng bài viết cho phép trong gói dịch vụ hiện tại.");
//        }
//
//        // Allowed to post
//        Post post = new Post();
//        post.setTitle(postRequest.getTitle());
//        post.setDescription(postRequest.getDescription());
//        post.setImg_Urls(postRequest.getImgUrls());
//        post.setCreatedAt(LocalDateTime.now());
//        post.setWoodworkerProfile(woodworkerProfile);
//
//        Post savedPost = postRepository.save(post);
//        return convertToPostRes(savedPost);
//    }

    @Override
    public PostRes getPostById(Long postId) {
        return postRepository.findById(postId)
                .map(this::convertToPostRes)
                .orElse(null); // return null if not found
    }

    @Override
    public List<PostRes> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToPostRes)
                .collect(Collectors.toList());
    }

    @Override
    public PostRes updatePost(Long postId, PostRequest postRequest) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(postRequest.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("WoodworkerProfile not found with ID: " + postRequest.getWoodworkerId()));

        existingPost.setTitle(postRequest.getTitle());
        existingPost.setDescription(postRequest.getDescription());
        existingPost.setImg_Urls(postRequest.getImgUrls());
        existingPost.setWoodworkerProfile(woodworkerProfile);

        Post updatedPost = postRepository.save(existingPost);
        return convertToPostRes(updatedPost);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        postRepository.delete(post);
    }

    private PostRes convertToPostRes(Post post) {
        return PostRes.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .imgUrls(post.getImg_Urls())
                .createdAt(post.getCreatedAt())
                .woodworkerId(post.getWoodworkerProfile().getWoodworkerId())
                .woodworkerName(post.getWoodworkerProfile().getBrandName())
                .build();
    }

    @Override
    public List<PostRes> getPostsByWoodworkerId(Long woodworkerId) {
        List<Post> posts = postRepository.findByWoodworkerProfile_WoodworkerId(woodworkerId);
        return posts.stream()
                .map(this::convertToPostRes)
                .collect(Collectors.toList());
    }

}
