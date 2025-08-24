package likelion.branders.Repository;

import likelion.branders.Entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {

    // 특정 이메일과 코드로 조회
    Optional<VerificationCodeEntity> findByEmailAndCode(String email, String code);


    void deleteByEmail(String email);
}