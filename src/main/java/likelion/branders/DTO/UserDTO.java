package likelion.branders.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private Long userId;
    private String loginId;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String birthDate;
}
