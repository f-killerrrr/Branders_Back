package likelion.branders.Controller;

import jakarta.servlet.http.HttpSession;
import likelion.branders.DTO.GlobalResponse;
import likelion.branders.DTO.UserDTO;
import likelion.branders.DTO.UserLoginDTO;
import likelion.branders.Service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userinfo")
public class UserLoginController {
    private final UserLoginService userLoginService;

    // 회원가입
    @PostMapping("/register")
    public GlobalResponse<UserDTO> register(@RequestBody UserLoginDTO userLoginDTO,
                                            HttpSession session) {
        // 세션에서 인증된 이메일 가져오기
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail == null) {
            return GlobalResponse.of("400", "이메일 인증이 필요합니다.");
        }

        UserDTO registeredUser = userLoginService.register(
                userLoginDTO.getLoginId(),
                userLoginDTO.getPassword(),
                userLoginDTO.getConfirmPassword(),
                userLoginDTO.getAge(),
                userLoginDTO.getFounderStatus(),
                verifiedEmail,          // 세션에서 가져온 이메일
                userLoginDTO.getLocation()
        );

        return GlobalResponse.of("200", "회원가입 완료", registeredUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO loginRequest) {
        UserDTO loggedInUser = userLoginService.login(
                loginRequest.getLoginId(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(loggedInUser);
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userLoginService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

}
