package entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailsEntity {
    private int orderDetailsId;
    private int orderId;
    private int productId;
    private int quantity;
}
