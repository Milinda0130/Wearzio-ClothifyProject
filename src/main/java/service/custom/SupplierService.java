package service.custom;

import dto.Supplier;
import service.SuperService;

import java.util.List;

public interface SupplierService extends SuperService {
    List<Supplier> getSuppliers();
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(int supplierId);

}
