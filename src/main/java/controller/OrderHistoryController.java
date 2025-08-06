package controller;

import dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceFactory;
import service.custom.*;
import util.ServiceType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {
    private final OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERS);
    private final OrderDetailsService orderDetailService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERPRODUCT);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    private final LoginSignupService loginSignupService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    private ObservableList<OrderHistory> orderHistoryItems = FXCollections.observableArrayList();
    @FXML
    private TableColumn colCustomerName;

    @FXML
    private TableColumn colEmployeeName;

    @FXML
    private TableColumn colOrderDate;

    @FXML
    private TableColumn colOrderId;

    @FXML
    private TableColumn colPaymentType;

    @FXML
    private TableColumn colProductName;

    @FXML
    private TableColumn colQuantity;

    @FXML
    private TableColumn colTotalAmount;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private TableView  tblOrderHistory;

    @FXML
    private TextField txtSearchOrder;

    @FXML
    void btnSearchOrderHistory(ActionEvent event) {
        String searchText = txtSearchOrder.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            tblOrderHistory.setItems(orderHistoryItems);
            return;
        }
        ObservableList<OrderHistory> filteredList = orderHistoryItems.filtered(order -> matchesSearch(order, searchText));
        tblOrderHistory.setItems(filteredList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("userName"));

        populateTable();
    }

    private boolean matchesSearch(OrderHistory order, String searchText) {
        return String.valueOf(order.getOrderId()).contains(searchText) ||
                (order.getOrderDate() != null && order.getOrderDate().toString().toLowerCase().contains(searchText)) ||
                (order.getProductName() != null && order.getProductName().toLowerCase().contains(searchText)) ||
                (order.getCustomerName() != null && order.getCustomerName().toLowerCase().contains(searchText)) ||
                (order.getUserName() != null && order.getUserName().toLowerCase().contains(searchText)) ||
                (order.getPaymentMethod() != null && order.getPaymentMethod().toLowerCase().contains(searchText));
    }

    private void populateTable() {
        try {
            List<Order> orders = orderService.getOrders();
            System.out.println("orders: " + orders);

            for (Order order : orders) {
                if (order == null || order.getOrderDetailsList() == null) continue;

                // Load customer and employee only once per order
                Customer customer = customerService.getCustomerById(order.getCustomerId());
                User user = loginSignupService.getUserById(order.getUserId());

                for (OrderDetails orderDetail : order.getOrderDetailsList()) {
                    if (orderDetail == null ) continue;

                    Product product = productService.getProductById(orderDetail.getProductId());

                    OrderHistory orderHistory = new OrderHistory(
                            order.getId(),
                            order.getDate(),
                            product != null ? product.getName() : "null",
                            product != null ? product.getPrice() : 0.0,
                            orderDetail.getQuantity(),
                            (product != null ? product.getPrice() * orderDetail.getQuantity() : 0.0),
                            order.getPaymentMethod() != null ? order.getPaymentMethod() : "null",
                            customer != null ? customer.getName() : "null",
                            user != null ? user.getUserName() : "null"
                    );

                    orderHistoryItems.add(orderHistory);
                }
            }

            tblOrderHistory.setItems(orderHistoryItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
