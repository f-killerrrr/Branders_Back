package likelion.branders.DAO;

import likelion.branders.DTO.UserDTO;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDAO {
    private final UserRepository userRepository;

    public UserDTO getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Invalid Id"))
                .toDTO();
    }

    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getUserId()).orElseThrow(() -> new RuntimeException("Invalid User ID"));
        user.setLoginId(userDTO.getLoginId());
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setBirthDate(userDTO.getBirthDate());
        return userRepository.save(user).toDTO();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO addUser(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.builder()
                .loginId(userDTO.getLoginId())
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .birthDate(userDTO.getBirthDate())
                .build();

        return userRepository.save(userEntity).toDTO();
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserEntity::toDTO)
                .toList();
    }
}

