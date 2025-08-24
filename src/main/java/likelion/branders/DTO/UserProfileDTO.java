package likelion.branders.DTO;

import likelion.branders.Enum.FounderStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String userName;
    private String loginId;
    private String email;
    private String password;
    private String birthDate;
    private FounderStatus founderStatus;
    private String location;
    private String desiredIndustry;
}
