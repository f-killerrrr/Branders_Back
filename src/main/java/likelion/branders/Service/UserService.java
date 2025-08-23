package likelion.branders.Service;

import likelion.branders.DAO.UserDAO;
import likelion.branders.DTO.UserDTO;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final UserRepository userRepository;

    public UserDTO addUser(UserDTO userDTO) {
        return userDAO.addUser(userDTO);
    }


    public UserDTO getUser(Long userId){
        return userDAO.getUser(userId);
    }

    public UserDTO updateUser(UserDTO userDTO){
        return userDAO.updateUser(userDTO);
    }

    public void deleteUser(Long userId){
        userDAO.deleteUser(userId);
    }


    public List<UserDTO> getAllUsers() {
        return userDAO.getAllUsers();
    }

    //마이페이지
    public UserDTO getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return user.toDTO();
    }
}
