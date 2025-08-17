package likelion.branders.DAO;

import jakarta.persistence.EntityManager;
import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.ChatSessionRepository;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor

public class ChatSessionDAO {
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;

    //세션 생성
    public ChatSessionEntity createSession(String title, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: " + userId));

        ChatSessionEntity session = ChatSessionEntity.builder()
                .sessionTitle(title)
                .user(user)
                .build();

        return chatSessionRepository.save(session);
    }

    // 세션 단건 조회
    public ChatSessionEntity getSession(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대화세션을 찾을 수 없습니다: " + sessionId));
    }

    // 특정 유저의 모든 세션 조회
    public List<ChatSessionEntity> getSessionsByUser(Long userId) {
        return chatSessionRepository.findByUser_UserId(userId);
    }
}
