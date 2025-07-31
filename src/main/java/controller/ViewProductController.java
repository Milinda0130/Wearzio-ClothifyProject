package controller;

import dto.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.ProductService;
import util.ServiceType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ViewProductController {

    private final ProductService service = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private List<Product> productList;

    @FXML
    private FlowPane flowPaneProductsManagement;

    @FXML
    private ScrollPane paneCardView;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    public void initialize() {
        productList = service.getProducts();
        loadProductCards(productList);
    }

    private void loadProductCards(List<Product> products) {
        flowPaneProductsManagement.getChildren().clear();
        flowPaneProductsManagement.setHgap(15);
        flowPaneProductsManagement.setVgap(15);
        flowPaneProductsManagement.setPrefWrapLength(950);

        for (Product product : products) {
            VBox card = createProductCard(product);
            flowPaneProductsManagement.getChildren().add(card);
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(5);
        card.setStyle("-fx-padding: 12; -fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(97,97,97,0.2), 15, 0, 0, 0);");
        card.setPrefWidth(182);
        card.setPrefHeight(300);
        card.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();

        try {
            String imagePath = "/images/products/" + product.getImage(); // assuming getImage() returns only filename
            InputStream imageStream = getClass().getResourceAsStream(imagePath);

            if (imageStream != null) {
                imageView.setImage(new Image(imageStream));
            } else {
                System.out.println("Image not found in resources: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading image: " + e.getMessage());
        }


        imageView.setFitWidth(182);
        imageView.setFitHeight(130);

        Rectangle clip = new Rectangle(182, 130);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        Label lblName = new Label(product.getName());
        lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label lblCategory = new Label("Category: " + product.getCategory());
        Label lblQty = new Label("Stock: " + product.getQuantityOnHand());

        Label lblPrice = new Label("LKR " + product.getPrice());
        lblPrice.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox infoBox = new VBox(3, lblName, lblCategory, lblQty, lblPrice);
        infoBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, infoBox);

        return card;
    }

    @FXML
    void btnAddProductOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddProduct.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Add Product");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Add Product window!").show();
        }
    }

    @FXML
    void btnSearchProductOnAction(ActionEvent event) {
        String search = txtSearchProduct.getText().trim().toLowerCase();
        List<Product> filtered = new ArrayList<>();

        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(search)) {
                filtered.add(product);
            }
        }
        loadProductCards(filtered);
    }

    @FXML
    void btnSortAccessoriesOnAction(ActionEvent event) {
        sortByCategory("Accessories");
    }

    @FXML
    void btnSortAllProductsOnAction(ActionEvent event) {
        loadProductCards(productList);
    }

    @FXML
    void btnSortFootwareOnAction(ActionEvent event) {
        sortByCategory("Footwear");
    }

    @FXML
    void btnSortGentsOnAction(ActionEvent event) {
        sortByCategory("Gents");
    }

    @FXML
    void btnSortKidsOnAction(ActionEvent event) {
        sortByCategory("Kids");
    }

    @FXML
    void btnSortLadiesOnAction(ActionEvent event) {
        sortByCategory("Ladies");
    }

    private void sortByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filtered.add(product);
            }
        }
        loadProductCards(filtered);
    }
}
