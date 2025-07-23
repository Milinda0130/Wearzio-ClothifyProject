package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Label lblForgitPassword;

    @FXML
    private TextField txtLoginEmail;

    @FXML
    private PasswordField txtLoginPassword;

    @FXML
    void btnLogInOnAction(ActionEvent event) {

    }

    @FXML
    void lblForgotPasswordMouseEntered(MouseEvent event) {
        lblForgitPassword.setStyle("-fx-text-fill: #a30000;");
    }

    @FXML
    void lblForgotPasswordMouseExited(MouseEvent event) {

    }

    @FXML
    void lblForgotPasswordOnAction(MouseEvent event) {

    }

}
