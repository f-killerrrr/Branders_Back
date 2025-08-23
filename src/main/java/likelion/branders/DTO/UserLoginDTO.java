package likelion.branders.DTO;

import likelion.branders.Enum.FounderStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserLoginDTO {
    private String loginId;
    private String password;
    private String confirmPassword;
    private Integer age;
    private FounderStatus founderStatus;
    private String email; // 이메일 인증 후 받은 값
    private String location;
}
