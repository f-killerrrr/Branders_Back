package likelion.branders.Entity;

import jakarta.persistence.*;
import likelion.branders.DTO.ChatMessageDTO;
import likelion.branders.DTO.ChatSessionDTO;
import likelion.branders.Enum.SenderType;
import lombok.*;
import org.w3c.dom.Text;


@Entity
@Table(name = "chatmessage")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @ManyToOne
    @JoinColumn(name = "sessionId", nullable = false)
    private ChatSessionEntity session;

    public ChatMessageDTO toDTO(){
        return ChatMessageDTO.builder()
                .messageId(messageId)
                .message(message)
                .senderType(senderType)
                .sessionId(session.getSessionId())
                .build();
    }
}
