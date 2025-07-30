package controller;

import dto.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewCustomer implements Initializable {

    @FXML
    private TableColumn<Customer, Integer> colCustomerId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colMobileNumber;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private AnchorPane panelViewCustomer;

    @FXML
    private TableView<Customer> tblCustomer;

    @FXML
    private TextField txtSearchCustomer;

    private CustomerService customerService;
    private ObservableList<Customer> allCustomers;
    private ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    @FXML
    void btnAddCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddCustomer.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Add Customer");
            stage.setResizable(false);
            stage.show();

            // Refresh table when window closes
            stage.setOnHidden(e -> loadCustomers());

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Add Customer window!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchCustomer(ActionEvent event) {
        searchCustomers();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize service
            customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

            if (customerService == null) {
                new Alert(Alert.AlertType.ERROR, "Failed to initialize Customer Service!").show();
                return;
            }

            // Set up table columns
            if (colCustomerId != null) {
                colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
            }
            if (colName != null) {
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            }
            if (colMobileNumber != null) {
                colMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobile"));
            }
            if (colAddress != null) {
                colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            }

            // Load customers
            loadCustomers();

            // Add double-click listener to table
            if (tblCustomer != null) {
                tblCustomer.setRowFactory(tv -> {
                    TableRow<Customer> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !row.isEmpty()) {
                            Customer selectedCustomer = row.getItem();
                            openCustomerDetailPopup(selectedCustomer);
                        }
                    });
                    return row;
                });
            }

            // Add search functionality
            if (txtSearchCustomer != null) {
                txtSearchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
                    searchCustomers();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Initialization Error: " + e.getMessage()).show();
        }
    }

    private void loadCustomers() {
        try {
            if (customerService != null) {
                allCustomers = FXCollections.observableArrayList(customerService.getCustomers());
                filteredCustomers.setAll(allCustomers);
                if (tblCustomer != null) {
                    tblCustomer.setItems(filteredCustomers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers: " + e.getMessage()).show();
        }
    }

    private void searchCustomers() {
        try {
            if (txtSearchCustomer == null || allCustomers == null) {
                return;
            }

            String searchText = txtSearchCustomer.getText().toLowerCase().trim();

            if (searchText.isEmpty()) {
                filteredCustomers.setAll(allCustomers);
            } else {
                filteredCustomers.clear();
                for (Customer customer : allCustomers) {
                    if (customer != null && (
                            (customer.getName() != null && customer.getName().toLowerCase().contains(searchText)) ||
                                    (customer.getMobile() != null && customer.getMobile().toLowerCase().contains(searchText)) ||
                                    (customer.getAddress() != null && customer.getAddress().toLowerCase().contains(searchText)) ||
                                    String.valueOf(customer.getId()).contains(searchText))) {
                        filteredCustomers.add(customer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCustomerDetailPopup(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UpdateCustomer.fxml"));
            AnchorPane pane = loader.load();

            UpdateCustomer controller = loader.getController();
            if (controller != null) {
                controller.setCustomerData(customer);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Customer Details");
            stage.setResizable(false);
            stage.show();

            // Refresh table when window closes
            stage.setOnHidden(e -> loadCustomers());

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Customer Details window!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }
}