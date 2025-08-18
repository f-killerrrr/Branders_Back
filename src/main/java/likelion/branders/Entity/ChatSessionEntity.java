package likelion.branders.Entity;

import jakarta.persistence.*;
import likelion.branders.DTO.ChatSessionDTO;
import likelion.branders.DTO.UserDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageEntity> messages = new ArrayList<>();

    public ChatSessionDTO toDTO(){
        return ChatSessionDTO.builder()
                .sessionId(sessionId)
                .sessionTitle(sessionTitle)
                .userId(user.getUserId())
                .messages(messages.stream()
                        .map(ChatMessageEntity::toDTO)
                        .toList())
                .build();
    }
}
