package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PostRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PostRes;
import java.util.List;

public interface PostService {
    PostRes createPost(PostRequest postRequest);
    PostRes getPostById(Long postId);
    List<PostRes> getAllPosts();
    PostRes updatePost(Long postId, PostRequest postRequest);
    void deletePost(Long postId);
    List<PostRes> getPostsByWoodworkerId(Long woodworkerId);
}
