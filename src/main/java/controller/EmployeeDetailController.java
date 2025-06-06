package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

public class EmployeeDetailController {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtJobrole;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtid;

    EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);



public void setEmployeeData(Employee employee) {
              txtid.setText(employee.getId().toString());
            txtName.setText(employee.getName());
            txtEmail.setText(employee.getEmail());
            txtJobrole.setText(employee.getJobRole());
        }

    public void btnUpdate(ActionEvent actionEvent) {

          Integer id =Integer.parseInt(txtid.getText());
        String name = txtName.getText();
        String email = txtEmail.getText();
        String jobRole = txtJobrole.getText();



        Employee updatedEmployee = new Employee(id, name, email, jobRole);



      Boolean update =   employeeService.updateEmployee(updatedEmployee);

      if (update== true){
          new Alert(Alert.AlertType.CONFIRMATION,"Update Successfully").show();
      clearFields();
      }
      else {

          new Alert(Alert.AlertType.ERROR,"Cant Update").show();
      }
    }

    public void btnDelete(ActionEvent actionEvent) {

    Integer id =  Integer.parseInt(txtid.getText());

    Boolean delete = employeeService.deleteEmployee(id);

    if (delete==true){

        new Alert(Alert.AlertType.CONFIRMATION,"Delete Successfully").show();
        clearFields();
    }
    else {

        new Alert(Alert.AlertType.ERROR,"Can't Delete").show();
            clearFields();
    }


    }
    public void  clearFields(){

        txtid.clear();
        txtName.clear();
        txtEmail.clear();
        txtJobrole.clear();
    }
}

