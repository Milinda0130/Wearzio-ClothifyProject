package controller;

import com.jfoenix.controls.JFXButton;
import dto.Employee;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.LoginSignupService;
import service.custom.impl.LoginSignupServiceImpl;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Signup implements Initializable {

    public TextField txtjobrole;
    public ComboBox cmbRole;
    @FXML
    private JFXButton btnSignup;

    @FXML
    private Label lblAlreadyAccount;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtname;
    LoginSignupService loginSignupService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.getItems().addAll("Cashier", "Admin");
    }
    @FXML
    void btnsignupOnAction(ActionEvent event) {


        String name = txtname.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String selectedRole =  (String)cmbRole.getValue();
        if (txtname.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty() || txtConfirmPassword.getText().trim().isEmpty() || cmbRole.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Create User");
            alert.setHeaderText("All fields must be filled!");
            alert.show();
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Password Mismatch");
            alert.setHeaderText("Passwords do not match!");
            alert.show();
            return;
        }
         User user = new User(name,email,password,selectedRole);


        Boolean b = loginSignupService.addNewUser(user);

        if (b==true) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully!").show();
            clearFields();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to add customer.").show();


        }



        clearFields();
    }

    @FXML
    void lblAlreadyAccountEntered(MouseEvent event) {

    }

    @FXML
    void lblAlreadyAccountExited(MouseEvent event) {

    }

    @FXML
    void lblAlreadyAccountOnAction(MouseEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Login.fxml"))));
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void  clearFields(){

        txtname.clear();
        txtname.clear();
        txtEmail.clear();
        txtConfirmPassword.clear();
    }


}
