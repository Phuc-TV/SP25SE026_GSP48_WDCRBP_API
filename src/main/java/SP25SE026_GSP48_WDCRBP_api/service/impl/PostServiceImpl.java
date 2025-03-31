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
        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findById(postRequest.getWoodworkerId())
                .orElseThrow(() -> new RuntimeException("WoodworkerProfile not found with ID: " + postRequest.getWoodworkerId()));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setImg_Urls(postRequest.getImgUrls());
        post.setCreatedAt(LocalDateTime.now());
        post.setWoodworkerProfile(woodworkerProfile);

        Post savedPost = postRepository.save(post);
        return convertToPostRes(savedPost);
    }

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
}
