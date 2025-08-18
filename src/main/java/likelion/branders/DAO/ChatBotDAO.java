package likelion.branders.DAO;

import likelion.branders.Entity.ChatMessageEntity;
import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Repository.ChatMessageRepository;
import likelion.branders.Repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor

public class ChatBotDAO {
    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;

    // 세션 저장
    public ChatSessionEntity saveSession(ChatSessionEntity session) {
        return sessionRepository.save(session);
    }

    // 메시지 저장
    public ChatMessageEntity saveMessage(ChatMessageEntity message) {
        return messageRepository.save(message);
    }

    // 유저 세션 조회
    public List<ChatSessionEntity> findSessionsByUserId(Long userId) {
        return sessionRepository.findByUserUserId(userId);
    }

    // 세션별 메시지 조회
    public List<ChatMessageEntity> findMessagesBySessionId(Long sessionId) {
        return messageRepository.findBySessionSessionId(sessionId);
    }

    // 세션 단건 조회
    public ChatSessionEntity findSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));
    }
}
