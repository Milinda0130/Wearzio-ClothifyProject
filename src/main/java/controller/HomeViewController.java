package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {
    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblWearzio;

    @FXML
    private Label lblClothingStore;

    private static User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

    }

    @FXML
    private AnchorPane panelHome;

    @FXML
    void btnCustomerManagementOnAction(ActionEvent event) {
        AnchorPane anchorPane = null;
        try {
            anchorPane = new FXMLLoader().load(getClass().getResource("../view/ViewCustomer.fxml"));
            panelHome.getChildren().clear();
            panelHome.getChildren().add(anchorPane);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnDashboardOnAction(ActionEvent event) {

    }


    @FXML
    void btnEmployeeManagementOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewCustomer.fxml"));
            AnchorPane anchorPane = loader.load();
            panelHome.getChildren().clear();
            panelHome.getChildren().add(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOrderManagementOnAction(ActionEvent event) {

    }

    @FXML
    void btnProcustManagementOnAction(ActionEvent event) {

    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {

    }

    @FXML
    void btnSupplierManagementOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            animateLabel(lblWelcome, 0);
            animateLabel(lblWearzio, 300);
            animateLabel(lblClothingStore, 600);
    }
    private void animateLabel(Label label, int delayMillis) {
        // Fade Transition
        FadeTransition fade = new FadeTransition(Duration.millis(1500), label);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setDelay(Duration.millis(delayMillis));

        // Translate Transition (moves the label slightly upwards)
        TranslateTransition translate = new TranslateTransition(Duration.millis(1500), label);
        translate.setFromY(20);
        translate.setToY(0);
        translate.setDelay(Duration.millis(delayMillis));

        fade.play();
        translate.play();
    }
}
