package likelion.branders.DAO;

import likelion.branders.Entity.ChatMessageEntity;
import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Enum.SenderType;
import likelion.branders.Repository.ChatMessageRepository;
import likelion.branders.Repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor

public class ChatMessageDAO {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;

    // 메시지 저장
    public ChatMessageEntity saveMessage(Long sessionId, String message, SenderType senderType) {
        ChatSessionEntity session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대화세션을 찾을 수 없습니다: " + sessionId));

        ChatMessageEntity chatMessage = ChatMessageEntity.builder()
                .message(message)
                .senderType(senderType)
                .session(session)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    // 세션 내 메시지들 조회
    public List<ChatMessageEntity> getMessagesBySession(Long sessionId) {
        return chatMessageRepository.findBySession_SessionId(sessionId);
    }
}
