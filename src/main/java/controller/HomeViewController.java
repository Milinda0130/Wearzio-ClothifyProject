package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomeViewController {
    private static User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;

    }

    @FXML
    private AnchorPane panelHome;

    @FXML
    void btnCustomerManagementOnAction(ActionEvent event) {

    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {

    }


    @FXML
    void btnEmployeeManagementOnAction(ActionEvent event) {
        AnchorPane anchorPane = null;
        try {
            anchorPane = new FXMLLoader().load(getClass().getResource("../view/ManageEmployeeView.fxml"));
            panelHome.getChildren().clear();
            panelHome.getChildren().add(anchorPane);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
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

}
