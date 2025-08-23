package likelion.branders.Service;

import jakarta.transaction.Transactional;
import likelion.branders.DTO.UserDTO;
import likelion.branders.DTO.UserLoginDTO;
import likelion.branders.Entity.ChatSessionEntity;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Entity.VerificationCodeEntity;
import likelion.branders.Enum.FounderStatus;
import likelion.branders.Repository.ChatMessageRepository;
import likelion.branders.Repository.ChatSessionRepository;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserLoginService {

    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 회원가입
    public UserDTO register(String loginId, String password, String confirmPassword,
                                Integer age, FounderStatus founderStatus,String email,String location) {

        // 1. 아이디 중복 확인
        if (userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 2. 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. UserEntity 생성
        UserEntity user = UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .age(age)
                .founderStatus(founderStatus)
                .location(location)
                .email(email)       // 인증 완료된 이메일
                .emailVerified(true) // 이메일 인증 완료
                .build();

        // 4. 저장
        UserEntity saved = userRepository.save(user);

        return saved.toDTO();
    }

    // 로그인
    public UserDTO login(String loginId, String password) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        // 비밀번호 비교
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user.toDTO();
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        // 1. 해당 유저의 모든 세션 조회
        List<ChatSessionEntity> sessions = chatSessionRepository.findByUserUserId(userId);

        for (ChatSessionEntity session : sessions) {
            // 2. 세션에 포함된 메시지 모두 삭제
            chatMessageRepository.deleteAll(session.getMessages());
            // 3. 세션 삭제
            chatSessionRepository.delete(session);
        }

        // 4. 유저 삭제
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        userRepository.delete(user);
    }

    private UserEntity userDTOToEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .loginId(dto.getLoginId())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .age(dto.getAge())
                .founderStatus(dto.getFounderStatus())
                .desiredIndustry(dto.getDesiredIndustry())
                .location(dto.getLocation())
                .emailVerified(dto.getEmailVerified() != null ? dto.getEmailVerified() : false)
                .build();
    }
}
