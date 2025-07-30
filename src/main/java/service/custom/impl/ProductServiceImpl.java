package service.custom.impl;

import dao.DaoFactory;
import dao.custom.ProductDao;
import dto.Product;
import entity.ProductEntity;
import org.modelmapper.ModelMapper;
import service.custom.ProductService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private static ProductServiceImpl productServiceImpl;
    private final ProductDao dao;
    private final ModelMapper modelMapper;

    public ProductServiceImpl() {
        dao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
        modelMapper = new ModelMapper();
    }

    public static ProductServiceImpl getInstance() {
        if (productServiceImpl == null) {
            productServiceImpl = new ProductServiceImpl();
        }
        return productServiceImpl;
    }
    @Override
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try {
            List<ProductEntity> productEntities = dao.getAll();
            for (ProductEntity entity : productEntities) {
                products.add(modelMapper.map(entity, Product.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean addProduct(Product product) {
        try {
            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
            return dao.save(productEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        try {
            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
            return dao.update(productEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        try {
            return dao.delete(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProductById(Integer productId) {
        ProductEntity productEntity = dao.search(productId);
        return modelMapper.map(productEntity, Product.class);

    }
}
