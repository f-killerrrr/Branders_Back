package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatsession")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String sessionTitle;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;
}
