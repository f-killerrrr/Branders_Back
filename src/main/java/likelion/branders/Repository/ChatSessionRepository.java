package likelion.branders.Repository;

import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {
    List<ChatSessionEntity> findByUser(UserEntity user);

    List<ChatSessionEntity> findByUser_UserId(Long userId);
}
