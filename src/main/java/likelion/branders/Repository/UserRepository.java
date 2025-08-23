package likelion.branders.Repository;

import likelion.branders.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId); // 로그인 시 사용
    boolean existsByLoginId(String loginId);           // 회원가입 시 중복 확인

}
