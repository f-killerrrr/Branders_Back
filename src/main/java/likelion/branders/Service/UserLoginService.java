package likelion.branders.Service;

import likelion.branders.DTO.UserDTO;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserLoginService {

    private final UserRepository userRepository;

    // 회원가입
    public UserDTO register(UserDTO userDTO) {
        // 1. 아이디 중복 체크
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        UserEntity saved = userRepository.save(userDTOToEntity(userDTO));

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

    private UserEntity userDTOToEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .loginId(dto.getLoginId())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .build();
    }
}
