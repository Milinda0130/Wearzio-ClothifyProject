package dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int userID;
    private String userName;
    private String email;
    private String password;
    private String role;
}
