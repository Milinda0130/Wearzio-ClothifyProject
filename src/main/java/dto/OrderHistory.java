package dto;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderHistory {
    private int orderId;
    private Date orderDate;
    private String productName;
    private double productPrice;
    private int quantity;
    private double totalAmount;
    private String paymentMethod;
    private String customerName;
    private String userName;
}
