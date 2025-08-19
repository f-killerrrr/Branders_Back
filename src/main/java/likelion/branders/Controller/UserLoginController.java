package likelion.branders.Controller;

import likelion.branders.DTO.UserDTO;
import likelion.branders.Service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserLoginController {
    private final UserLoginService userLoginService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userLoginService.register(userDTO);
        return ResponseEntity.ok(savedUser);
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
}
