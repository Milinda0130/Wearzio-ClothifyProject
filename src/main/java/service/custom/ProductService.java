package service.custom;

import dto.Product;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {
    List<Product> getProducts();
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(Integer productId);
    Product getProductById(Integer productId);
}
