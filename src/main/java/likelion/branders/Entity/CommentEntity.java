package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String commentContent;

    // 단방향 N:1 (Comment → Post)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")  // FK
    private PostEntity Post;

    // 단방향 N:1 (Comment → User)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")  // FK
    private UserEntity user;
}
