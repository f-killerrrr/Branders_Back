package likelion.branders.Service;

import likelion.branders.DAO.UserDAO;
import likelion.branders.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
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
}
