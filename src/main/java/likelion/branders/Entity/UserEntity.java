package likelion.branders.Entity;

import jakarta.persistence.*;
import likelion.branders.DTO.UserDTO;
import likelion.branders.DTO.UserProfileDTO;
import likelion.branders.Enum.FounderStatus;
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private Integer age;

    @Column
    private String phoneNumber;

    @Column
    private String birthDate;

    //창업 여부 : 창업자, 예비창업자
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FounderStatus founderStatus;

    //창업 업종
    @Column
    private String desiredIndustry;

    //창업 위치
    @Column
    private String location;

    //이메일인증여부
    @Column(nullable = false)
    private Boolean emailVerified = false;


    public UserEntity(Long userId) {
        this.userId = userId;
    }

    public UserDTO toDTO(){
        return UserDTO.builder()
                .userId(userId)
                .loginId(loginId)
                .userName(userName)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .age(age)
                .founderStatus(founderStatus)
                .desiredIndustry(desiredIndustry)
                .emailVerified(emailVerified)
                .location(location)
                .build();
    }

    public UserProfileDTO toProfileDTO() {
        return UserProfileDTO.builder()
                .userName(this.userName)
                .loginId(this.loginId)
                .email(this.email)
                .password(this.password)
                .birthDate(this.birthDate)
                .founderStatus(this.founderStatus)
                .location(this.location)
                .desiredIndustry(this.desiredIndustry)
                .build();
    }
}
