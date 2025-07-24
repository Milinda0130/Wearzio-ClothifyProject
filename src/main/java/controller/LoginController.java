package controller;

import com.jfoenix.controls.JFXButton;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.LoginSignupService;
import util.ServiceType;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Label lblForgitPassword;

    @FXML
    private TextField txtLoginEmail;

    @FXML
    private PasswordField txtLoginPassword;
    LoginSignupService loginSignupService = ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnLogInOnAction(ActionEvent event) {
        if (txtLoginEmail.getText().isEmpty() || txtLoginPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Fields are empty");
            alert.show();
        } else {
            User loginUser = loginSignupService.login(txtLoginEmail.getText(), txtLoginPassword.getText());
            if (loginUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Username or Password is incorrect");
                alert.show();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Home.fxml"));
                    Stage stage = (Stage) txtLoginEmail.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.setTitle("Clothify");
                    stage.setResizable(false);
                    stage.centerOnScreen();

                    HomeViewController homeViewController = loader.getController();
                    homeViewController.setCurrentUser(loginUser);

                    stage.show();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Unable to load the home view.");
                    alert.show();
                }
            }
        }
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
