package likelion.branders.Repository;

import likelion.branders.Entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {

    List<ChatSessionEntity> findByUserUserId(Long userId);
}
