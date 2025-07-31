package controller;

import dto.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import service.ServiceFactory;

import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateSupplierController implements Initializable {
    public TextField txtSupplierId;
    private SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);


    @FXML
    private ComboBox<String> cmbSupplyItem;

    @FXML
    private TextField txtSupplierCompany;

    @FXML
    private TextField txtSupplierEmail;

    @FXML
    private TextField txtSupplierName;

    @FXML
    void btnSupplierDelete(ActionEvent event) {
        try {
            if (txtSupplierId.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Supplier ID!").show();
                return;
            }

            int id = Integer.parseInt(txtSupplierId.getText().trim());

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this supplier?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response.getText().equals("OK")) {
                    boolean result = supplierService.deleteSupplier(id);
                    if (result) {
                        new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete Supplier!").show();
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
        void btnSupplierUpdate(ActionEvent event) {
        try {
            if (txtSupplierId.getText().trim().isEmpty() ||
                    txtSupplierName.getText().trim().isEmpty() ||
                    cmbSupplyItem.getSelectionModel().getSelectedItem() == null) {
                new Alert(Alert.AlertType.WARNING, "Please fill all required fields and select an item!").show();
                return;
            }

            int id = Integer.parseInt(txtSupplierId.getText().trim());
            String name = txtSupplierName.getText().trim();
            String company = txtSupplierCompany.getText().trim();
            String email = txtSupplierEmail.getText().trim();
            String item = cmbSupplyItem.getSelectionModel().getSelectedItem();

            Supplier supplier = new Supplier(id, name, company, email, item);

            System.out.println("Updating supplier: " + supplier);

            boolean result = supplierService.updateSupplier(supplier);

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Supplier!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid ID number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    public void setSupplierData(Supplier supplier) {
        if (supplier != null) {
            txtSupplierId.setText(String.valueOf(supplier.getId()));
            txtSupplierName.setText(supplier.getName());
            txtSupplierCompany.setText(supplier.getSupplierCompany());

            txtSupplierEmail.setText(supplier.getEmail());
            cmbSupplyItem.getSelectionModel().select(supplier.getItem());
        }
    }

    public void clearFields() {
        txtSupplierId.clear();
        txtSupplierCompany.clear();
        txtSupplierEmail.clear();
        txtSupplierName.clear();
        cmbSupplyItem.getSelectionModel().clearSelection();
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbSupplyItem.getItems().add("Gents");
        cmbSupplyItem.getItems().add("Ladies");
        cmbSupplyItem.getItems().add("Kids");
        cmbSupplyItem.getItems().add("Accessories");
        cmbSupplyItem.getItems().add("Footwear");
    }
}


