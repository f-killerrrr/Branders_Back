package likelion.branders.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    // HTML 메일 발송
    public void sendEmail(String toEmail, String title, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content, true); // HTML 허용
        helper.setFrom("dlehdgus2283@gmail.com"); // 보내는 사람 주소

        try {
            emailSender.send(message);
        } catch (RuntimeException e) {
            log.error("메일 전송 실패", e);
            throw new RuntimeException("Unable to send email in sendEmail", e);
        }
    }
}

