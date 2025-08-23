package likelion.branders.Service;

import jakarta.transaction.Transactional;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Entity.VerificationCodeEntity;
import likelion.branders.Repository.UserRepository;
import likelion.branders.Repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    // 인증 메일 발송 (이전 코드 삭제 + 5분 유효)
    public void sendCodeToEmail(String email) {
        // 1️⃣ 기존 코드 삭제
        verificationCodeRepository.deleteByEmail(email);

        // 2️⃣ 새 인증 코드 생성
        String code = generateRandomCode(6);
        VerificationCodeEntity vc = VerificationCodeEntity.builder()
                .email(email)
                .code(code)
                .expiresTime(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5)) // 5분 유효
                .verified(false)
                .build();
        verificationCodeRepository.save(vc);

        // 3️⃣ 이메일 내용 작성 및 발송
        String title = "Branders 이메일 인증 번호";
        String content = "<html><body>"
                + "<h1>Branders 인증 코드: " + code + "</h1>"
                + "<p>홈페이지에 입력하세요.</p>"
                + "<footer style='color: grey; font-size: small;'>"
                + "<p>※본 메일은 자동응답 메일이므로 회신하지 마세요.</p>"
                + "</footer></body></html>";

        try {
            emailService.sendEmail(email, title, content);
        } catch (Exception e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }

    // 이메일 인증 (5분 유효)
    public boolean verifyCode(String email, String code) {
        VerificationCodeEntity vc = verificationCodeRepository.findByEmailAndCode(email, code)
                .orElse(null);

        if (vc == null) {
            System.out.println("인증 코드 없음: email=" + email + ", code=" + code);
            return false;
        }

        LocalDateTime nowUTC = LocalDateTime.now(ZoneOffset.UTC);

        // 만료 확인
        if (vc.getExpiresTime().isBefore(nowUTC)) {
            verificationCodeRepository.delete(vc); // 만료 코드 삭제
            System.out.println("인증 코드 만료: " + code);
            return false;
        }

        // 인증 완료 표시
        vc.setVerified(true);
        verificationCodeRepository.save(vc);
        verificationCodeRepository.delete(vc); // 인증 후 코드 삭제

        System.out.println("이메일 인증 완료: " + email);
        return true;
    }

    // 랜덤 코드 생성기
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
