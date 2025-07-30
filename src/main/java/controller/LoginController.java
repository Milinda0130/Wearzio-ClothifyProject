package controller;

import com.jfoenix.controls.JFXButton;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.LoginSignupService;
import util.ServiceType;

import java.io.IOException;

public class LoginController {

    @FXML public AnchorPane paneLogin;
    @FXML private JFXButton btnLogin;
    @FXML private Label lblForgitPassword;
    @FXML public TextField txtLoginEmail;
    @FXML private PasswordField txtLoginPassword;

    private final LoginSignupService loginSignupService =
            ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    void btnLogInOnAction(ActionEvent event) {
        String email = txtLoginEmail.getText().trim();
        String password = txtLoginPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Fields cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }

        try {
            User loginUser = loginSignupService.login(email, password);
            if (loginUser != null) {
                loadHomeView(loginUser, event);
            } else {
                showAlert("Login Failed", "Invalid credentials", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("System Error", "Unable to connect to server: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadHomeView(User user, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());

            HomeViewController controller = loader.getController();
            controller.setCurrentUser(user);

            stage.setScene(scene);
            stage.setTitle("Wearzio");
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load home screen: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void lblForgotPasswordOnAction(MouseEvent event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(""));
            stage.setScene(new Scene(root));
            stage.setTitle("Password Recovery");
            stage.setResizable(false);
            stage.show();

            // Close current window
            Stage currentStage = (Stage) lblForgitPassword.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load password recovery: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void lblForgotPasswordMouseEntered(MouseEvent event) {
        lblForgitPassword.setStyle("-fx-text-fill: #a30000; -fx-cursor: hand;");
    }

    @FXML
    void lblForgotPasswordMouseExited(MouseEvent event) {
        lblForgitPassword.setStyle("-fx-text-fill: #000000;");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}