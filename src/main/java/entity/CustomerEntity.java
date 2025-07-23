package entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerEntity {
    private int id;
    private String name;
    private String mobile;
    private String address;
}
