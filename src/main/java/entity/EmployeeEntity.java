package entity;


import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeEntity {
private Integer id;
private String  name;
private String email;
private String jobRole;
}
