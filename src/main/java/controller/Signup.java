package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import service.custom.LoginSignupService;
import service.custom.impl.LoginSignupServiceImpl;

public class Signup {

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

    @FXML
    void btnsignupOnAction(ActionEvent event) {
        if (txtname.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty() || txtConfirmPassword.getText().trim().isEmpty()){

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
    }

    @FXML
    void lblAlreadyAccountEntered(MouseEvent event) {

    }

    @FXML
    void lblAlreadyAccountExited(MouseEvent event) {

    }

    @FXML
    void lblAlreadyAccountOnAction(MouseEvent event) {

    }

}
