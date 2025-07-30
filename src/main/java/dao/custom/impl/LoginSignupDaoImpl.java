package dao.custom.impl;

import dao.custom.LoginSignUpDao;
import db.DBConnection;
import entity.UserEntity;
import org.jasypt.util.text.BasicTextEncryptor;
import service.custom.LoginSignupService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSignupDaoImpl implements LoginSignUpDao {
    private static LoginSignupDaoImpl loginSignupDaoImpl;

    public static LoginSignupDaoImpl getInstance() {
        if (loginSignupDaoImpl == null) {
            loginSignupDaoImpl = new LoginSignupDaoImpl();
        }
        return loginSignupDaoImpl;
    }
    @Override
    public UserEntity login(String email, String password) {
        String sql = "SELECT password FROM user WHERE email = ?";
        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                textEncryptor.setPassword("ClothifySecureKey");

                String decryptedPassword = resultSet.getString("password");
//                String decryptedPassword = textEncryptor.decrypt(passwordFromDB);

                if (password.trim().equals(decryptedPassword)) {
                    String query = "SELECT * FROM user WHERE email = ?";
                    PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement(query);
                    statement2.setString(1, email);

                    ResultSet resultSet2 = statement2.executeQuery();
                    if (resultSet2.next()) {
                        return new UserEntity(
                                resultSet2.getInt("userId"),
                                resultSet2.getString("userName"),
                                resultSet2.getString("email"),
                                resultSet2.getString("password"),

                                resultSet2.getString("role")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public UserEntity updatePassword(String email, String password) {
        try {
            String query = "UPDATE user SET password = ? WHERE email = ?";
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            boolean isPasswordUpdated = preparedStatement.executeUpdate() > 0;

            if (isPasswordUpdated) {
                String sql = "SELECT * FROM user WHERE email = ?";
                PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement2.setString(1, email);

                ResultSet resultSet2 = statement2.executeQuery();
                if (resultSet2.next()) {
                    return new UserEntity(
                            resultSet2.getInt("userId"),
                            resultSet2.getString("name"),
                            resultSet2.getString("email"),
                            "xxxxx",
                            resultSet2.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserEntity getUserById(int id) {
        String query = "SELECT * FROM user WHERE userId = ?";

        try {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserEntity(
                        resultSet.getInt("userId"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        "xxxxx",
                        resultSet.getString("role")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveUser(UserEntity userEntity) {
        if (userEntity != null) {
            String sql = "INSERT INTO user (userName, email, password, role) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1,userEntity.getUserName());
                statement.setString(2,userEntity.getEmail());
                statement.setString(3,userEntity.getPassword());
                statement.setString(4,userEntity.getRole());
                return statement.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
