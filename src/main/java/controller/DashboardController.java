package controller;

import com.jfoenix.controls.JFXButton;
import dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.OrderService;
import service.custom.ProductService;
import util.ServiceType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardController implements Initializable {

    // Services
    private CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    private ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERS);

    // Static variables
    private static User currentUser;

    // Instance variables
    private List<Product> productList = new ArrayList<>();
    private List<Product> cartList = new ArrayList<>();

    // FXML Components
    @FXML
    private JFXButton btnPayBill;

    @FXML
    private ComboBox<String> cmbSelectCustomer;

    @FXML
    private FlowPane flowPaneCart;

    @FXML
    private FlowPane flowPaneCart1;

    @FXML
    private FlowPane flowPaneProducts;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblLoadOrderId;

    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane panePlaceOrder;

    @FXML
    private TextField txtSearchProduct;

    @FXML
    private Label txtTotalAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing Dashboard Controller...");

        try {
            // Load current date and time
            setCurrentDateTime();

            // Load customers into combo box
            loadCustomersComboBox();

            // Load products
            loadProducts();

            // Load order ID
            loadOrderId();

            // Initialize total amount
            txtTotalAmount.setText("0.00");

            System.out.println("Dashboard Controller initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing Dashboard Controller: " + e.getMessage());
        }
    }

    private void setCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        lblDate.setText(now.format(dateFormatter));
        lblTime.setText(now.format(timeFormatter));
    }

    private void loadOrderId() {
        // Generate a simple order ID (you can modify this logic based on your needs)
        int orderId = 1000 + (int)(Math.random() * 9000);
        lblLoadOrderId.setText(String.valueOf(orderId));
    }

    private void loadProducts() {
        try {
            productList = productService.getProducts();
            System.out.println("Loaded " + productList.size() + " products");

            if (productList != null && !productList.isEmpty()) {
                loadProductPanes(productList);
            } else {
                System.out.println("No products found or product list is empty");
                // Show message to user
                showInfoAlert("No Products", "No products available to display");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading products: " + e.getMessage());
            showErrorAlert("Error", "Failed to load products: " + e.getMessage());
        }
    }

    private void loadCustomersComboBox() {
        try {
            List<Customer> customerList = customerService.getCustomers();
            ObservableList<String> customerObservableList = FXCollections.observableArrayList();

            if (customerList != null) {
                for (Customer customer : customerList) {
                    customerObservableList.add(customer.getId() + " - " + customer.getName() + " - " + customer.getMobile());
                }
            }

            cmbSelectCustomer.setItems(customerObservableList);
            System.out.println("Loaded " + customerObservableList.size() + " customers");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private void loadProductPanes(List<Product> products) {
        try {
            flowPaneProducts.getChildren().clear();
            flowPaneProducts.setHgap(15);
            flowPaneProducts.setVgap(15);
            flowPaneProducts.setPrefWrapLength(950);

            System.out.println("Creating product cards for " + products.size() + " products");

            for (Product product : products) {
                try {
                    VBox productCard = createProductCard(product);
                    flowPaneProducts.getChildren().add(productCard);
                } catch (Exception e) {
                    System.err.println("Error creating card for product: " + product.getName() + " - " + e.getMessage());
                }
            }

            System.out.println("Successfully loaded " + flowPaneProducts.getChildren().size() + " product cards");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading product panes: " + e.getMessage());
        }
    }

    private VBox createProductCard(Product product) {
        VBox productCard = new VBox(5);
        productCard.setStyle("-fx-padding: 12; -fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(97,97,97,0.2), 15, 0, 0, 0);");
        productCard.setPrefWidth(177);
        productCard.setPrefHeight(265);
        productCard.setAlignment(Pos.CENTER);

        // Create image view
        ImageView productImage = new ImageView();
        loadProductImage(productImage, product);

        productImage.setFitWidth(177);
        productImage.setFitHeight(130);
        productImage.setPreserveRatio(false);

        Rectangle clip = new Rectangle(177, 130);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        productImage.setClip(clip);

        // Create labels
        Label lblProductName = new Label(product.getName() != null ? product.getName() : "Unknown Product");
        lblProductName.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label lblCategory = new Label("Category: " + (product.getCategory() != null ? product.getCategory() : "N/A"));
        Label lblQuantity = new Label("Stock: " + product.getQuantityOnHand());

        Label lblPrice = new Label("LKR " + product.getPrice());
        lblPrice.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox textContainer = new VBox(3, lblProductName, lblCategory, lblQuantity);
        textContainer.setAlignment(Pos.CENTER);

        // Set click action
        setProductCardClickAction(productCard, product);

        productCard.getChildren().addAll(productImage, textContainer, lblPrice);

        return productCard;
    }

    private void loadProductImage(ImageView imageView, Product product) {
        try {
            // First try to load from file system (as in your original code)
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                File imageFile = new File(product.getImage());
                if (imageFile.exists()) {
                    imageView.setImage(new Image(imageFile.toURI().toString()));
                    return;
                }

                // Try to load from resources
                String imagePath = "/images/products/" + product.getImage();
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    imageView.setImage(new Image(imageStream));
                    return;
                }
            }

            // Load placeholder image if product image not found
            InputStream placeholderStream = getClass().getResourceAsStream("/images/placeholder.png");
            if (placeholderStream != null) {
                imageView.setImage(new Image(placeholderStream));
            } else {
                // Create a simple colored rectangle as fallback
                imageView.setStyle("-fx-background-color: #f0f0f0;");
            }

        } catch (Exception e) {
            System.err.println("Error loading image for product " + product.getName() + ": " + e.getMessage());
            imageView.setStyle("-fx-background-color: #f0f0f0;");
        }
    }

    private void setProductCardClickAction(VBox productCard, Product product) {
        productCard.setOnMouseClicked(event -> {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Quantity");
                dialog.setHeaderText("Enter the quantity for " + product.getName());
                dialog.setContentText("Quantity:");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int quantity = Integer.parseInt(input);

                        if (quantity <= 0) {
                            showErrorAlert("Invalid Quantity", "Please enter a positive quantity.");
                            return;
                        }

                        Product currentProduct = productService.getProductById(product.getId());
                        if (currentProduct == null) {
                            showErrorAlert("Product Error", "Product not found.");
                            return;
                        }

                        boolean isProductAvailable = currentProduct.getQuantityOnHand() >= quantity;

                        if (!isProductAvailable) {
                            showErrorAlert("Stock Error", "Not enough stock available. Available stock: " + currentProduct.getQuantityOnHand());
                            return;
                        }

                        // Check if product already exists in cart
                        boolean found = false;
                        for (Product cartProduct : cartList) {
                            if (cartProduct.getId() == product.getId()) {
                                int newQuantity = cartProduct.getQuantityOnHand() + quantity;
                                if (newQuantity <= currentProduct.getQuantityOnHand()) {
                                    cartProduct.setQuantityOnHand(newQuantity);
                                    found = true;
                                } else {
                                    showErrorAlert("Stock Error", "Total quantity exceeds available stock.");
                                    return;
                                }
                                break;
                            }
                        }

                        if (!found) {
                            cartList.add(new Product(
                                    product.getId(),
                                    product.getName(),
                                    product.getCategory(),
                                    product.getSize(),
                                    product.getPrice(),
                                    quantity,
                                    product.getImage(),
                                    product.getSupplierId()
                            ));
                        }

                        loadCartPane();
                        calculateTotal();
                        showInfoAlert("Success", "Product added to cart successfully!");

                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid Input", "Please enter a valid number.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("Error", "An error occurred while adding the product to cart.");
            }
        });
    }

    private void loadCartPane() {
        try {
            if (flowPaneCart != null) {
                flowPaneCart.getChildren().clear();

                for (Product product : cartList) {
                    VBox cartItem = createCartItem(product);
                    flowPaneCart.getChildren().add(cartItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading cart pane: " + e.getMessage());
        }
    }

    private VBox createCartItem(Product product) {
        VBox cartItem = new VBox(5);
        cartItem.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 1;");

        Label lblName = new Label(product.getName());
        Label lblQuantity = new Label("Qty: " + product.getQuantityOnHand());
        Label lblPrice = new Label("LKR " + (product.getPrice() * product.getQuantityOnHand()));

        Button btnRemove = new Button("Remove");
        btnRemove.setOnAction(e -> {
            cartList.remove(product);
            loadCartPane();
            calculateTotal();
        });

        cartItem.getChildren().addAll(lblName, lblQuantity, lblPrice, btnRemove);
        return cartItem;
    }

    private void calculateTotal() {
        double total = 0.0;
        for (Product product : cartList) {
            total += product.getPrice() * product.getQuantityOnHand();
        }
        txtTotalAmount.setText(String.format("%.2f", total));
    }

    // Event handlers
    @FXML
    void btnAddNewCustomerOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddCustomer.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Add Customer");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Failed to open Add Customer window!");
        }
    }

    @FXML
    void btnPayBillOnAction(ActionEvent event) {
        try {
            if (cartList.isEmpty()) {
                showErrorAlert("Order Error", "Please add products to cart first.");
                return;
            }

            List<OrderDetails> orderDetailsList = new ArrayList<>();
            for (Product product : cartList) {
                OrderDetails orderDetails = new OrderDetails(
                        1,
                        Integer.parseInt(lblLoadOrderId.getText()),
                        product.getId(),
                        product.getQuantityOnHand()
                );
                orderDetailsList.add(orderDetails);
            }

            Order order = new Order(
                    Integer.parseInt(lblLoadOrderId.getText()),
                    convertDateFormat(lblDate.getText()),
                    Double.parseDouble(txtTotalAmount.getText()),
                    "Cash",
                    currentUser != null ? currentUser.getUserId() : 1,
                    cmbSelectCustomer.getSelectionModel().getSelectedItem() != null ? getCustomerIdByComboBox(cmbSelectCustomer.getSelectionModel().getSelectedItem().toString()) : 0, orderDetailsList);

            boolean isOrderPlaced = orderService.addOrder(order);

            if (isOrderPlaced) {
                showInfoAlert("Success", "Order placed successfully!");

                // Refresh products
                loadProducts();

                // Clear cart and reset form
                cartList.clear();
                loadCartPane();
                calculateTotal();
                cmbSelectCustomer.getSelectionModel().clearSelection();
                loadOrderId();

            } else {
                showErrorAlert("Order Error", "Failed to place order. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred while placing the order: " + e.getMessage());
        }
    }

    @FXML
    void btnSearchProductOnAction(ActionEvent event) {
        try {
            String searchText = txtSearchProduct.getText().trim().toLowerCase();
            List<Product> filteredList = new ArrayList<>();

            if (!searchText.isEmpty()) {
                for (Product product : productList) {
                    if (product.getName().toLowerCase().contains(searchText)) {
                        filteredList.add(product);
                    }
                }
                loadProductPanes(filteredList);
            } else {
                loadProductPanes(productList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Error occurred while searching products.");
        }
    }

    @FXML
    void btnSortAllProductsOnAction(ActionEvent event) {
        loadProductPanes(productList);
    }

    @FXML
    void btnSortGentsOnAction(ActionEvent event) {
        loadProductPanes(sortProductsByCategory("Gents"));
    }

    @FXML
    void btnSortLadiesOnAction(ActionEvent event) {
        loadProductPanes(sortProductsByCategory("Ladies"));
    }
    @FXML
    void btnFootwearOnAction(ActionEvent event) {
loadProductPanes(sortProductsByCategory("footwear"));
    }
    @FXML
    void btnSortKidsOnAction(ActionEvent event) {
        loadProductPanes(sortProductsByCategory("Kids"));
    }

    @FXML
    void btnSortAccessoriesOnAction(ActionEvent event) {
        loadProductPanes(sortProductsByCategory("Accessories"));
    }

    // Helper methods
    private List<Product> sortProductsByCategory(String category) {
        List<Product> sortedList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                sortedList.add(product);
            }
        }
        return sortedList;
    }

    public static Integer getCustomerIdByComboBox(String selectedValue) {
        if (selectedValue == null || selectedValue.isEmpty()) {
            return 0;
        }
        String pattern = "^(\\d+)\\s*-";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(selectedValue);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    public static Date convertDateFormat(String inputDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(inputDate, inputFormatter);
            return Date.valueOf(localDate);
        } catch (Exception e) {
            e.printStackTrace();
            return Date.valueOf(LocalDate.now());
        }
    }

    // Utility methods for alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


}