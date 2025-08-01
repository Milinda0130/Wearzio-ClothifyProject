package controller;

import dto.Product;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

    public class UpdateProductController {

        @FXML
        private ComboBox<?> cmbAddProcutSupplierId;

        @FXML
        private ComboBox<?> cmbAddProductCategory;

        @FXML
        private ComboBox<?> cmbAddProductSize;

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

        }

        @FXML
        void btnAddSupplierOnAction(ActionEvent event) {

        }

        @FXML
        void btnUpdateProductOnAction(ActionEvent event) {

        }


        public void setProductData(Product product) {
        }
    }