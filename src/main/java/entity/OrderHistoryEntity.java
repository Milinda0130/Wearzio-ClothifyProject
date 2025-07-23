package entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderHistoryEntity {
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
