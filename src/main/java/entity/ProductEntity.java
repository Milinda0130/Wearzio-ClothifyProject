package entity;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductEntity {
    private Integer id;
    private String name;
    private String size;
    private Double price;
    private  Integer quantityOnHand;
    private String category;
    private String image;
    private Integer supplierId;

}
