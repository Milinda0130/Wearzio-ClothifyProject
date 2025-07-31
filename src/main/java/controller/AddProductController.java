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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

public class AddProductController implements Initializable {
    ProductService service = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProductCategoryComboBox();
        loadProductSizeComboBox();
        loadProductSupplierComboBox();
        supplierService.getSuppliers();
    }
    @FXML
    private ComboBox cmbAddProcutSupplierId;

    @FXML
    private ComboBox  cmbAddProductCategory;

    @FXML
    private ComboBox  cmbAddProductSize;

    @FXML
    private TextField txtAddProductImagePath;

    @FXML
    private TextField txtAddProductName;

    @FXML
    private TextField txtAddProductPrice;

    @FXML
    private TextField txtAddProductQuantityOnHand;

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
                // Destination directory
                File destDir = new File("src/main/resources/images/products");

                // Create directory if not exists
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                // Get the file name
                String fileName = selectedFile.getName();

                // Copy the image to your local project folder
                File destFile = new File(destDir, fileName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Set only file name in the text field (to save in DB)
                txtAddProductImagePath.setText(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Image Copy Error");
                alert.setHeaderText("Failed to copy image to project folder.");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }


    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AddSupplier.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Add Supplier");
        stage.show();
        supplierService.getSuppliers();
    }

    @FXML
    void btnSaveProductOnAction(ActionEvent event) {
        if (txtAddProductImagePath.getText().trim().isEmpty() ||
                txtAddProductName.getText().trim().isEmpty() ||
                txtAddProductPrice.getText().trim().isEmpty() ||
                txtAddProductQuantityOnHand.getText().trim().isEmpty() ||
                cmbAddProductCategory.getValue() == null ||
                cmbAddProductSize.getValue() == null ||
                cmbAddProcutSupplierId.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fields Can't be Empty");
            alert.show();
            return;
        }
        boolean isProductAdded = service.addProduct(new Product(
                1,
                txtAddProductName.getText(),
                cmbAddProductCategory.getSelectionModel().getSelectedItem().toString(),
                cmbAddProductSize.getSelectionModel().getSelectedItem().toString(),
                Double.parseDouble(txtAddProductPrice.getText()),
                Integer.parseInt(txtAddProductQuantityOnHand.getText()),
                txtAddProductImagePath.getText(),
                Integer.parseInt(cmbAddProcutSupplierId.getSelectionModel().getSelectedItem().toString().split(" - ")[0])
        ));

        if (isProductAdded) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Adding Successfull");
            alert.setHeaderText("Product Adding Successfull");
            alert.show();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Product Adding Failed");
            alert.setHeaderText("Product Adding Failed");
            alert.show();
        }
    }

    private void loadProductSupplierComboBox() {
        List<Supplier> supplierList = supplierService.getSuppliers();
        ObservableList<String> supplierObservableList = FXCollections.observableArrayList();
        for (Supplier supplier : supplierList) {
            supplierObservableList.add(supplier.getId() + " - " + supplier.getName() + " - " + supplier.getSupplierCompany());
        }
        cmbAddProcutSupplierId.setItems(supplierObservableList);
    }

    private void loadProductCategoryComboBox() {
        List<String> productCategories = List.of("Gents", "Ladies", "Kids", "Accessories", "Footwear", "Other");
        cmbAddProductCategory.setItems(FXCollections.observableArrayList(productCategories));
    }

    private void loadProductSizeComboBox() {
        List<String> productSizes = List.of("XXS", "XS", "Small", "Medium", "Large", "XL", "XXL", "XXXL");
        cmbAddProductSize.setItems(FXCollections.observableArrayList(productSizes));
    }

}
