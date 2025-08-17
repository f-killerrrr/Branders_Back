package likelion.branders.Controller;

import likelion.branders.DAO.UserDAO;
import likelion.branders.DTO.UserDTO;
import likelion.branders.Entity.UserEntity;
import likelion.branders.Repository.UserRepository;
import likelion.branders.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserDAO userDAO;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() { // 모두 조회
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) { //id 기반 조회
        UserDTO userDTO = userService.getUser(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) { //생성
        UserDTO userEntity = userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }
    @PutMapping("/{userId}") //업데이트
    public UserDTO updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        // 경로 변수로 받은 userId를 DTO에 수동으로 설정
        userDTO.setUserId(userId);
        return userDAO.updateUser(userDTO);
    }

    @DeleteMapping("/{userId}") //삭제
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}