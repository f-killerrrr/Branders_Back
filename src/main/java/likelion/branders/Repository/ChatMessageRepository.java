package likelion.branders.Repository;

import likelion.branders.Entity.ChatMessageEntity;
import likelion.branders.Entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findBySession(ChatSessionEntity session);

    List<ChatMessageEntity> findBySession_SessionId(Long sessionId);

}
