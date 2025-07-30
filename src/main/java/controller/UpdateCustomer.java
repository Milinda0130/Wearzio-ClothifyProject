package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

public class UpdateCustomer {

    @FXML
    private JFXTextField txtMobile;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtid;

    private CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @FXML
    void btnDelete(ActionEvent event) {
        try {
            if (txtid.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter customer ID!").show();
                return;
            }

            int id = Integer.parseInt(txtid.getText().trim());

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response.getText().equals("OK")) {
                    boolean result = customerService.deleteCustomer(id);
                    if (result) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
                    }
                }
            });

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid ID number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdate(ActionEvent event) {
        try {
            if (txtid.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").show();
                return;
            }

            int id = Integer.parseInt(txtid.getText().trim());
            String name = txtName.getText().trim();
            String mobile = txtMobile.getText().trim();
            String address = txtAddress.getText().trim();

            Customer customer = new Customer(id, name, mobile, address);

            boolean result = customerService.updateCustomer(customer);

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid ID number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void setCustomerData(Customer customer) {
        if (customer != null) {
            txtid.setText(String.valueOf(customer.getId()));
            txtName.setText(customer.getName());
            txtMobile.setText(customer.getMobile());
            txtAddress.setText(customer.getAddress());
        }
    }

    public void clearFields() {
        txtid.clear();
        txtName.clear();
        txtMobile.clear();
        txtAddress.clear();
    }
}
