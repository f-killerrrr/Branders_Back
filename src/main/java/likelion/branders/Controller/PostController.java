package likelion.branders.Controller;
import likelion.branders.DTO.PostRequestDTO;
import likelion.branders.DTO.PostResponseDTO;
import likelion.branders.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // 게시글 생성 (POST)
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO newPost = postService.createPost(requestDTO);
        return ResponseEntity.ok(newPost);
    }

    // 모든 게시글 조회 (GET)
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 수정 (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO updatedPost = postService.updatePost(id, requestDTO);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
