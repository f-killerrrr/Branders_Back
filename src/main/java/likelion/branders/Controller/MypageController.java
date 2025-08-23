package likelion.branders.Controller;

import likelion.branders.DTO.GlobalResponse;
import likelion.branders.DTO.UserProfileDTO;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final UserRepository userRepository;

    //마이프로필
    @GetMapping("/{userId}")
    public GlobalResponse<UserProfileDTO> getMyPage(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return GlobalResponse.of("200", "마이페이지 조회 성공", user.toProfileDTO());
    }

    //내정보 수정
    @PutMapping("/{userId}")
    public GlobalResponse<UserProfileDTO> updateMyPage(
            @PathVariable Long userId,
            @RequestBody UserProfileDTO profileDTO) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 수정 가능 항목
        user.setUserName(profileDTO.getUserName());
        user.setBirthDate(profileDTO.getBirthDate());
        user.setFounderStatus(profileDTO.getFounderStatus());
        user.setLocation(profileDTO.getLocation());
        user.setDesiredIndustry(profileDTO.getDesiredIndustry());

        UserEntity saved = userRepository.save(user);

        return GlobalResponse.of("200", "마이페이지 수정 완료", saved.toProfileDTO());
    }
}
