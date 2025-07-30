package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtid.setDisable(true);
    }
}