package service.custom.impl;

import dto.User;
import service.custom.LoginSignupService;

public class LoginSignupServiceImpl implements LoginSignupService {

    @Override
    public boolean register(User dto) {
        return false;
    }

    @Override
    public boolean login(String email, String password) {
        return false;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        return false;
    }

    @Override
    public boolean sendOtp(String email) {
        return false;
    }
}

