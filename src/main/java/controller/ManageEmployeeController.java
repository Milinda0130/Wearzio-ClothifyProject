package controller;

import dto.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ManageEmployeeController implements Initializable {

    @FXML
    private TableColumn<?, ?> colEmployeeEmail;

    @FXML
    private TableColumn colEmployeeId;

    @FXML
    private TableColumn colEmployeeName;

    @FXML
    private TableColumn colJobRole;

    @FXML
    private AnchorPane panelHome;

    @FXML
    private TableView<Employee> tblEmployee;

    @FXML
    private TextField txtSearchEmployee;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    ObservableList<Employee> allEmployees;

    ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();


    @FXML
    void btnAddEmployee(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddEmployeeView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add Employee");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnSearchEmployee(ActionEvent event) {
        String searchText = txtSearchEmployee.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            employeeObservableList.setAll(allEmployees);  // Reset to full list
        } else {
            ObservableList<Employee> filteredList = FXCollections.observableArrayList();

            for (Employee emp : allEmployees) {
                String id = String.valueOf(emp.getId()).toLowerCase(); // convert int to string


                if (id.contains(searchText)){
                    filteredList.add(emp);
                }
            }

            employeeObservableList.setAll(filteredList);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployeeEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("jobRole"));

        allEmployees = FXCollections.observableArrayList(employeeService.getEmployees());
        employeeObservableList.setAll(allEmployees);
        tblEmployee.setItems(employeeObservableList);

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                openEmployeeDetailPopup(newValue);
            }
        });
    }

    private void openEmployeeDetailPopup(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeDetailView.fxml"));
            AnchorPane pane = loader.load();

            EmployeeDetailController controller = loader.getController();
            controller.setEmployeeData(employee);

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Employee Details");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
