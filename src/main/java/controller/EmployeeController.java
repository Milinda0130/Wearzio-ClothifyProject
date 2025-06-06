package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeController  {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtJobrole;

    @FXML
    private JFXTextField txtName;


    @FXML
    private JFXTextField txtid;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    @FXML
    void btnAdd(ActionEvent event) {

        Integer id = Integer.parseInt(txtid.getText());
        String name = txtName.getText();
        String email = txtEmail.getText();
        String jobRole = txtJobrole.getText();

        Employee newEmployee = new Employee(id, name, email, jobRole);


        Boolean b = employeeService.addEmployee(newEmployee);
        ;

        if (b==true) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully!").show();
            clearFields();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add customer.").show();


        }

    }
    public void  clearFields(){

        txtid.clear();
        txtName.clear();
        txtEmail.clear();
        txtJobrole.clear();
    }


}