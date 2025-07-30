package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

public class AddCustomer {

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtMobile;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtid;

    private CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @FXML
    void btnAdd(ActionEvent event) {
        try {
            // Validation
            if (txtName.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter customer name!").show();
                return;
            }

            if (txtMobile.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter mobile number!").show();
                return;
            }

            // For auto-increment ID, you might not need to parse txtid
            // But if you need it, validate it first
            int id = 0;
            if (!txtid.getText().trim().isEmpty()) {
                id = Integer.parseInt(txtid.getText().trim());
            }

            String name = txtName.getText().trim();
            String mobile = txtMobile.getText().trim();
            String address = txtAddress.getText().trim();

            Customer customer = new Customer(id, name, mobile, address);

            boolean result = customerService.addCustomer(customer);

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add Customer!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid ID number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void clearFields() {
        txtid.clear();
        txtName.clear();
        txtMobile.clear();
        txtAddress.clear();
    }
}