package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PostRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PostRes;
import SP25SE026_GSP48_WDCRBP_api.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public CoreApiResponse<PostRes> createPost(@Valid @RequestBody PostRequest postRequest) {
        try {
            PostRes response = postService.createPost(postRequest);
            return CoreApiResponse.success(response, "Tạo bài viết thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo bài viết thất bại" + e.getMessage());
        }

    }

    @GetMapping("/{postId}")
    public CoreApiResponse<PostRes> getPostById(@PathVariable Long postId) {
        try{
            PostRes response = postService.getPostById(postId);
            if (response == null) {
                return CoreApiResponse.success(null, "Không tìm thấy bài viết");
            }
            return CoreApiResponse.success(response, "Lấy bài viết thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy bài viết thất bại" + e.getMessage());
        }

    }

    @GetMapping
    public CoreApiResponse<List<PostRes>> getAllPosts() {
        try{
            List<PostRes> response = postService.getAllPosts();
            if (response == null || response.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy bài viết");
            }
            return CoreApiResponse.success(response, "Lấy tất cả bài viết thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy bài viết thất bại" + e.getMessage());
        }
    }

    @GetMapping("/woodworker/{woodworkerId}")
    public CoreApiResponse<List<PostRes>> getAllPostsByWwId(@PathVariable Long woodworkerId) {
        try{
            List<PostRes> response = postService.getAllPosts().stream()
                    .filter(post -> post.getWoodworkerId().equals(woodworkerId))
                    .toList();

            if (response.isEmpty()) {
                return CoreApiResponse.success(null, "Không tìm thấy bài viết");
            }

            return CoreApiResponse.success(response, "Lấy tất cả bài viết thành công");
        }catch (Exception e){
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lấy bài viết thất bại" + e.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public CoreApiResponse<PostRes> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest postRequest) {
        try {
        PostRes response = postService.updatePost(postId, postRequest);
        return CoreApiResponse.success(response, "Cập nhật bài viết thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Cập nhật bài viết thất bại" + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public CoreApiResponse<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return CoreApiResponse.success("Xóa bài viết thành công");
    }
}