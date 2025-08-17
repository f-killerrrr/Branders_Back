package likelion.branders.DTO;

import likelion.branders.Enum.SenderType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChatMessageDTO {
    private Long messageId;
    private String message;
    private SenderType senderType;
    private Long sessionId;
}
