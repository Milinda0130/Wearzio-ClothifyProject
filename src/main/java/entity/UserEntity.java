package entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {
    private int userID;
    private String userName;
    private String email;
    private String password;
    private String role;
}
