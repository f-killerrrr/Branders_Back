package likelion.branders.DTO;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChatSessionDTO {
    private Long sessionId;
    private String sessionTitle;
    private Long userId;
    private List<ChatMessageDTO> messages;
}
