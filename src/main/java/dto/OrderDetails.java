package dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetails {
    private int orderDetailsId;
    private int orderId;
    private int productId;
    private int quantity;
}
