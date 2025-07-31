package controller;

import dto.Customer;
import dto.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.EmployeeService;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewSupplierController implements Initializable {
    private SupplierService supplierService;
    private ObservableList<Supplier> allSuppliers;
    private ObservableList<Supplier> filteredSupplier = FXCollections.observableArrayList();


    @FXML
    private TableColumn colSupplierCompany;

    @FXML
    private TableColumn colSupplierEmail;

    @FXML
    private TableColumn colSupplierId;

    @FXML
    private TableColumn colSupplierName;

    @FXML
    private TableColumn colSypplyItem;

    @FXML
    private TableView tblSupplier;

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddSupplier.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Add Supplier");
            stage.setResizable(false);
            stage.show();

            // Refresh table when window closes
            stage.setOnHidden(e -> loadSuppliers());

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Add Supplier window!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize service
            supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

            if (supplierService == null) {
                new Alert(Alert.AlertType.ERROR, "Failed to initialize Supplier Service!").show();
                return;
            }

            // Set up table columns
            if (colSupplierId != null) {
                colSupplierId.setCellValueFactory(new PropertyValueFactory<>("id"));
            }
            if (colSupplierName != null) {
                colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
            }
            if (colSupplierCompany != null) {
                colSupplierCompany.setCellValueFactory(new PropertyValueFactory<>("supplierCompany"));
            }
            if (colSupplierEmail != null) {
                colSupplierEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            }
            if (colSypplyItem != null) {
                colSypplyItem.setCellValueFactory(new PropertyValueFactory<>("item"));
            }

            // Load customers
            loadSuppliers();

            // Add double-click listener to table
            if (tblSupplier != null) {
                tblSupplier.setRowFactory(tv -> {
                    TableRow<Supplier> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !row.isEmpty()) {
                            Supplier selectedSupplier = row.getItem();
                            openSupplierDetailPopup(selectedSupplier);
                        }
                    });
                    return row;
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Initialization Error: " + e.getMessage()).show();
        }
    }

    private void loadSuppliers() {
        try {
            if (supplierService != null) {
                allSuppliers = FXCollections.observableArrayList(supplierService.getSuppliers());
                filteredSupplier.setAll(allSuppliers);
                if (tblSupplier != null) {
                    tblSupplier.setItems(filteredSupplier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers: " + e.getMessage()).show();
        }
    }



    private void openSupplierDetailPopup(Supplier supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UpdateSupplier.fxml"));
            AnchorPane pane = loader.load();

            UpdateSupplierController controller = loader.getController();
            if (controller != null) {
                controller.setSupplierData(supplier);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Supplier Details");
            stage.setResizable(false);
            stage.show();

            // Refresh table when window closes
            stage.setOnHidden(e -> loadSuppliers());

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Supplier Details window!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }
}
