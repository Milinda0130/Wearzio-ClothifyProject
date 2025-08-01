package controller;

import dto.Product;
import dto.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.ProductService;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {

    @FXML
    private ComboBox<String> cmbAddProcutSupplierId;

    @FXML
    private ComboBox<String> cmbAddProductCategory;

    @FXML
    private ComboBox<String> cmbAddProductSize;

    @FXML
    private TextField txtAddProductImagePath;

    @FXML
    private TextField txtAddProductName;

    @FXML
    private TextField txtAddProductPrice;

    @FXML
    private TextField txtAddProductQuantityOnHand;

    private Product selectedProduct;

    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductCategoryComboBox();
        loadProductSizeComboBox();
        loadProductSupplierComboBox();
    }

    @FXML
    void btnAddProductImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                File destDir = new File("src/main/resources/images/products");

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                String fileName = selectedFile.getName();
                File destFile = new File(destDir, fileName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                txtAddProductImagePath.setText(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to copy image!").show();
            }
        }
    }

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AddSupplier.fxml"))));
            stage.setTitle("Add Supplier");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateProductOnAction(ActionEvent event) {
        if (selectedProduct == null) {
            new Alert(Alert.AlertType.ERROR, "No product selected for update!").show();
            return;
        }

        if (txtAddProductName.getText().trim().isEmpty() ||
                txtAddProductPrice.getText().trim().isEmpty() ||
                txtAddProductQuantityOnHand.getText().trim().isEmpty() ||
                cmbAddProductCategory.getValue() == null ||
                cmbAddProductSize.getValue() == null ||
                cmbAddProcutSupplierId.getValue() == null ||
                txtAddProductImagePath.getText().trim().isEmpty()) {

            new Alert(Alert.AlertType.ERROR, "Fields cannot be empty!").show();
            return;
        }

        boolean isUpdated = productService.updateProduct(new Product(
                selectedProduct.getId(),
                txtAddProductName.getText(),
                cmbAddProductCategory.getValue(),
                cmbAddProductSize.getValue(),
                Double.parseDouble(txtAddProductPrice.getText()),
                Integer.parseInt(txtAddProductQuantityOnHand.getText()),
                txtAddProductImagePath.getText(),
                Integer.parseInt(cmbAddProcutSupplierId.getValue().split(" - ")[0])
        ));

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Product updated successfully!").show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } else {
            new Alert(Alert.AlertType.ERROR, "Product update failed!").show();
        }
    }

    public void setProductData(Product product) {
        this.selectedProduct = product;

        txtAddProductName.setText(product.getName());
        txtAddProductPrice.setText(String.valueOf(product.getPrice()));
        txtAddProductQuantityOnHand.setText(String.valueOf(product.getQuantityOnHand()));
        txtAddProductImagePath.setText(product.getImage());
        cmbAddProductCategory.setValue(product.getCategory());
        cmbAddProductSize.setValue(product.getSize());

        // Load suppliers and then select the one matching the product
        List<Supplier> suppliers = supplierService.getSuppliers();
        for (Supplier supplier : suppliers) {
            String value = supplier.getId() + " - " + supplier.getName() + " - " + supplier.getSupplierCompany();
            cmbAddProcutSupplierId.getItems().add(value);

            if (supplier.getId() == product.getSupplierId()) {
                cmbAddProcutSupplierId.setValue(value);
            }
        }
    }

    private void loadProductCategoryComboBox() {
        List<String> categories = List.of("Gents", "Ladies", "Kids", "Accessories", "Footwear", "Other");
        cmbAddProductCategory.setItems(FXCollections.observableArrayList(categories));
    }

    private void loadProductSizeComboBox() {
        List<String> sizes = List.of("XXS", "XS", "Small", "Medium", "Large", "XL", "XXL", "XXXL");
        cmbAddProductSize.setItems(FXCollections.observableArrayList(sizes));
    }

    private void loadProductSupplierComboBox() {
        List<Supplier> suppliers = supplierService.getSuppliers();
        ObservableList<String> supplierObservableList = FXCollections.observableArrayList();
        for (Supplier supplier : suppliers) {
            supplierObservableList.add(supplier.getId() + " - " + supplier.getName() + " - " + supplier.getSupplierCompany());
        }
        cmbAddProcutSupplierId.setItems(supplierObservableList);
    }
}
