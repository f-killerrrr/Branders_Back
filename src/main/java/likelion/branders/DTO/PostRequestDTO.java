package likelion.branders.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO { //요청(C/U)
    private Long postId;
    private String postTitle;
    private String postContent;
    private Long userId; // User 엔티티의 ID만 사용
    private Long categoryId; // Category 엔티티의 ID만 사용
}
