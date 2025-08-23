package likelion.branders.Controller;

import jakarta.servlet.http.HttpSession;
import likelion.branders.DTO.GlobalResponse;
import likelion.branders.DTO.MemberEmailRequestDTO;
import likelion.branders.DTO.MemberEmailVerifyResponseDTO;
import likelion.branders.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MemberService memberService;

    //이메일전송
    @PostMapping("/sendEmail")
    public GlobalResponse<Void> sendEmail(@RequestBody MemberEmailRequestDTO requestDTO) {
        memberService.sendCodeToEmail(requestDTO.getEmail());
        return GlobalResponse.of("200", "이메일 전송 성공");
    }

    // 2. 이메일 인증
    @PostMapping("/verifyEmail")
    public GlobalResponse<Void> verifyEmail(@RequestBody MemberEmailVerifyResponseDTO requestDTO, HttpSession session) {
        boolean isVerified = memberService.verifyCode(
                requestDTO.getEmail(), requestDTO.getVerificationCode()
        );

        if (isVerified) {
            session.setAttribute("verifiedEmail", requestDTO.getEmail());
            return GlobalResponse.of("200", "이메일 인증 완료");
        } else {
            return GlobalResponse.of("400", "유효하지 않거나 만료된 인증 코드");
        }
    }
}
