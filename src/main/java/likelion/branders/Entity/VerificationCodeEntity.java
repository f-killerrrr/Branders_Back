package likelion.branders.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="verification_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 이메일에 보냈는지
    @Column(nullable = false)
    private String email;

    // 실제 인증코드
    @Column(nullable = false)
    private String code;

    // 만료 시간 (예: 5분 뒤)
    @Column(nullable = false)
    private LocalDateTime expiresTime;

    // 인증 완료 여부
    @Column(nullable = false)
    private boolean verified = false;

}
