package dto;

import lombok.*;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private int id;
    private Date date;
    private Double price;
    private String paymentMethod;
    private int userId;
    private int customerId;
    private List<OrderDetails> orderDetailsList;

}
