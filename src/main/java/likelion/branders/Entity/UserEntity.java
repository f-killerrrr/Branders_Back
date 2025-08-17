package likelion.branders.Entity;

import jakarta.persistence.*;
import likelion.branders.DTO.UserDTO;
import lombok.*;

@Entity
@Table(name = "user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String loginId;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String birthDate;

    public UserDTO toDTO(){
        return UserDTO.builder()
                .userId(userId)
                .longinId(loginId)
                .userName(userName)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();
    }
}
