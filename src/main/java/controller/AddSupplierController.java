package controller;

import dto.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.SupplierService;
import util.ServiceType;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSupplierController implements Initializable {
    SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
    @FXML
    private ComboBox<String> cmbSupplyItem;

    @FXML
    private TextField txtSupplierCompany;

    @FXML
    private TextField txtSupplierEmail;

    @FXML
    private TextField txtSupplierName;

    @FXML
    void btnSaveSupplierOnAction(ActionEvent event) {
        boolean isSupplierAdded = supplierService.addSupplier(new Supplier(
                1,
                txtSupplierName.getText(),
                txtSupplierCompany.getText(),
                txtSupplierEmail.getText(),
                cmbSupplyItem.getSelectionModel().getSelectedItem().toString()
        ));
        if (isSupplierAdded) {
            new Alert(Alert.AlertType.INFORMATION, "Supplier Added Successfully!").show();
            clearFields();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add Supplier!").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbSupplyItem.getItems().add("Gents");
        cmbSupplyItem.getItems().add("Ladies");
        cmbSupplyItem.getItems().add("Kids");
        cmbSupplyItem.getItems().add("Accessories");
        cmbSupplyItem.getItems().add("Footwear");
    }
    public void clearFields() {
        txtSupplierCompany.clear();
        txtSupplierEmail.clear();
        txtSupplierName.clear();
        cmbSupplyItem.getSelectionModel().clearSelection();    }
}