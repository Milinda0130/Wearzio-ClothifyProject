package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    private  Integer id;
    private String name;
    private String email;
    private String jobRole;

}
