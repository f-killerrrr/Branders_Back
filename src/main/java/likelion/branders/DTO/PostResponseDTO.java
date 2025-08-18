package likelion.branders.DTO;

import lombok.*;

// 응답 DTO
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long postId;
    private String postTitle;
    private String postContent;
    private String userName; // 유저 이름만 포함
    private String categoryName; // 카테고리 이름만 포함
}
