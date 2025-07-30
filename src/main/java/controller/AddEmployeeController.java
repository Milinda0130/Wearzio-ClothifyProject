package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

public class    AddEmployeeController {

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


        if (b==true) {
            new Alert(Alert.AlertType.INFORMATION, "Employee Added Successfully!").show();
            clearFields();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add Employee.").show();


        }

    }
    public void  clearFields(){

        txtid.clear();
        txtName.clear();
        txtEmail.clear();
        txtJobrole.clear();
    }


}