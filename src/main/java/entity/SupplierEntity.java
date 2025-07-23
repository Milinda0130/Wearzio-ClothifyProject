package entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierEntity {
    private int id;
    private String name;
    private String supplierCompany;
    private String email;
    private String item;
}
