package likelion.branders.Service;
import likelion.branders.DTO.PostRequestDTO;
import likelion.branders.DTO.PostResponseDTO;
import likelion.branders.Entity.CategoryEntity;
import likelion.branders.Entity.PostEntity;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.CategoryRepository;
import likelion.branders.Repository.PostRepository;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository; // User 엔티티를 조회하기 위해 필요
    private final CategoryRepository categoryRepository; // Category 엔티티를 조회하기 위해 필요
    // 게시글 생성
    @Transactional
    public PostResponseDTO createPost(PostRequestDTO requestDTO) {
        UserEntity user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        CategoryEntity category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        PostEntity post = new PostEntity();
        post.setPostTitle(requestDTO.getPostTitle());
        post.setPostContent(requestDTO.getPostContent());
        post.setUser(user);
        post.setCategory(category);

        PostEntity savedPost = postRepository.save(post);
        return new PostResponseDTO(
                savedPost.getPostId(),
                savedPost.getPostTitle(),
                savedPost.getPostContent(),
                savedPost.getUser().getUserName(),
                savedPost.getCategory().getCategoryName()
        );
    }

    // 모든 게시글 조회
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 게시글 ID로 조회
    public PostResponseDTO getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return convertToDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO requestDTO) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setPostTitle(requestDTO.getPostTitle());
        post.setPostContent(requestDTO.getPostContent());

        PostEntity updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Entity를 DTO로 변환하는 헬퍼 메서드
    private PostResponseDTO convertToDto(PostEntity post) {
        return new PostResponseDTO(
                post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getUser().getUserName(),
                post.getCategory().getCategoryName()
        );
    }
}
