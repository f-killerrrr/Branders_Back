package likelion.branders.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MemberEmailRequestDTO {
    private String email;             // 인증할 이메일
}
