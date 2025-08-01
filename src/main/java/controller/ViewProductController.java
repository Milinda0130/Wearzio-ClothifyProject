package controller;

import dto.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.io.InputStream;
import java.io.IOException;
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
        card.setPrefHeight(360);
        card.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();

        try {
            String imagePath = "/images/products/" + product.getImage();
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

        // Buttons
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");

        btnUpdate.setStyle("-fx-background-color: #2b1b17; -fx-text-fill: white; -fx-cursor: hand;");
        btnDelete.setStyle("-fx-background-color: #2b1b17; -fx-text-fill: white; -fx-cursor: hand;");

        btnUpdate.setOnAction(e -> openUpdateForm(product));
        btnDelete.setOnAction(e -> deleteProduct(product));

        VBox infoBox = new VBox(3, lblName, lblCategory, lblQty, lblPrice);
        infoBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox(5, btnUpdate, btnDelete);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(imageView, infoBox, buttonBox);

        return card;
    }

    private void openUpdateForm(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UpdateProduct.fxml"));
            AnchorPane pane = loader.load();

            UpdateProductController controller = loader.getController();
            controller.setProductData(product); // <-- You must implement this method

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Update Product");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Update Product window!").show();
        }
    }

    private void deleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            boolean isDeleted = service.deleteProduct(product.getId());

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully!").show();
                productList = service.getProducts();
                loadProductCards(productList);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete product!").show();
            }
        }
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
