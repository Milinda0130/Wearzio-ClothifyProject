package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Product {

    private Integer id;
    private String name;
    private String size;
    private Double price;
    private  Integer quantityOnHand;
    private String category;
    private String image;
    private Integer supplierId;
}
