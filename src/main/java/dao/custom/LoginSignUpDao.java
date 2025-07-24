package dao.custom;

import dao.SuperDao;
import entity.UserEntity;

public interface LoginSignUpDao extends SuperDao {
    UserEntity login(String email, String password);

    UserEntity updatePassword(String email, String password);

    UserEntity getUserById(int id);

    boolean saveUser(UserEntity userEntity);
}
