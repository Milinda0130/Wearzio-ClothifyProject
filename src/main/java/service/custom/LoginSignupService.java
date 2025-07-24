package service.custom;

import dto.User;

public interface LoginSignupService {

    boolean register(User dto);
    boolean login(String email, String password);
    boolean updatePassword(String email, String newPassword);
    boolean sendOtp(String email);
}
