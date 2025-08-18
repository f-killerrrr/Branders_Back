package likelion.branders.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    private String postTitle;

    @Column
    private String postContent;

    // 단방향 N:1 (Post → User)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")  // FK 컬럼
    private UserEntity user;

    // 단방향 N:1 (Post → Category)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")  // FK
    private CategoryEntity category;

}
