package service.custom.impl;

import dao.DaoFactory;
import dao.custom.LoginSignUpDao;
import dto.User;
import entity.UserEntity;
import org.modelmapper.ModelMapper;
import service.custom.LoginSignupService;
import util.DaoType;

public class LoginSignupServiceImpl implements LoginSignupService {
    private static LoginSignupServiceImpl loginSignupServiceImpl;
    private final LoginSignUpDao loginSignupDao;

    private LoginSignupServiceImpl() {
        loginSignupDao = DaoFactory.getInstance().getDao(DaoType.USER);
    }
    public static LoginSignupServiceImpl getInstance() {
        if (loginSignupServiceImpl == null) {
            loginSignupServiceImpl = new LoginSignupServiceImpl();
        }
        return loginSignupServiceImpl;
    }

    @Override
    public User login(String email, String password) {
        UserEntity userEntity = loginSignupDao.login(email, password);
        if (userEntity == null) {
            return null;
        }
        return new ModelMapper().map(userEntity, User.class);

    }

    @Override
    public User updatePassword(String email, String password) {
        return new ModelMapper().map(loginSignupDao.updatePassword(email, password), User.class);

    }

    @Override
    public User getUserById(int id) {
        UserEntity userEntity = loginSignupDao.getUserById(id);
        if (userEntity != null) {
            return new ModelMapper().map(userEntity, User.class);
        }
        return null;
    }

    @Override
    public boolean addNewUser(User newUser) {
        if (newUser != null) {
            UserEntity userEntity = new ModelMapper().map(newUser, UserEntity.class);
            return loginSignupDao.saveUser(userEntity);
        }

        return false;
    }
}

