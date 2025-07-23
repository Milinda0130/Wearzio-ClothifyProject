package entity;

import dto.OrderDetails;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderEntity {
    private int id;
    private Date date;
    private Double price;
    private String paymentMethod;
    private int userId;
    private int customerId;
    private List<OrderDetailsEntity> orderDetailsList;
}
