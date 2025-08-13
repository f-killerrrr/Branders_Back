package likelion.branders.Entity;

import jakarta.persistence.*;
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
    private Long UserId;

    @Column
    private String LoginId;

    @Column
    private String UserName;

    @Column
    private String Password;

    @Column
    private String Email;

    @Column
    private String PhoneNumber;

    @Column
    private String BirthDate;
}
