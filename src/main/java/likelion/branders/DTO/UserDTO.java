package likelion.branders.DTO;

import likelion.branders.Enum.FounderStatus;
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
    private String confirmPassword; //회원가입시 비밀번호확인
    private String email;
    private String phoneNumber;
    private String birthDate;
    private Integer age;
    private FounderStatus founderStatus;  // 창업 여부
    private String desiredIndustry;       // 희망 창업 업종
    private Boolean emailVerified;        // 이메일 인증 여부
    private String location;
}
