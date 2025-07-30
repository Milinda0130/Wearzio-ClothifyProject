package service.custom.impl;

import dao.DaoFactory;
import dao.custom.SuplierDao;
import dto.Supplier;
import entity.SupplierEntity;
import org.modelmapper.ModelMapper;
import service.custom.SupplierService;
import util.DaoType;

import java.util.ArrayList;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    private static SupplierServiceImpl supplierServiceImpl;
    private final SuplierDao supplierDao;
    private final ModelMapper modelMapper;

    private SupplierServiceImpl() {
        supplierDao = DaoFactory.getInstance().getDao(DaoType.SUPPLIER);
        modelMapper = new ModelMapper();
    }

    public static SupplierServiceImpl getInstance() {
        if (supplierServiceImpl == null) {
            supplierServiceImpl = new SupplierServiceImpl();
        }
        return supplierServiceImpl;
    }

    @Override
    public List<Supplier> getSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try {
            List<SupplierEntity> supplierEntities = supplierDao.getAll();
            for (SupplierEntity entity : supplierEntities) {
                suppliers.add(modelMapper.map(entity, Supplier.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    @Override
    public boolean addSupplier(Supplier supplier) {
        try {
            SupplierEntity supplierEntity = modelMapper.map(supplier, SupplierEntity.class);
            return supplierDao.save(supplierEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        try {
            SupplierEntity supplierEntity = modelMapper.map(supplier, SupplierEntity.class);
            return supplierDao.update(supplierEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSupplier(int supplierId) {
        try {
            return supplierDao.delete(supplierId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
