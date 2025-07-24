package service.custom;

import dao.SuperDao;
import dto.User;
import service.SuperService;

public interface LoginSignupService extends SuperService {

    User login(String email, String password);

    User updatePassword(String email, String password);

    User getUserById(int id);

    boolean addNewUser(User newUser);
}
