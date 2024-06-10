package com.gd.todo.service;

import com.gd.todo.dao.PasswordTokenDao;
import com.gd.todo.dao.TokenDao;
import com.gd.todo.dao.UserDao;
import com.gd.todo.entity.PasswordToken;
import com.gd.todo.entity.Token;
import com.gd.todo.entity.User;
import com.gd.todo.model.PasswordModel;
import com.gd.todo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordTokenDao passwordTokenDao;

    public User registUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole("USER");
        userDao.save(user);
        return user;
    }

    public void saveToken(User user, String token) {
        Token tokenEntity = new Token(user, token);
        tokenDao.save(tokenEntity);
    }

    public String verifyToken(String token) {
        Token tokenData = tokenDao.findByToken(token);
        if (tokenData == null) {
            return "invalid";
        }
        User user = tokenData.getUser();
        Calendar cal = Calendar.getInstance();
        if (tokenData.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
            return "expired";
        }
        user.setActive(true);
        userDao.save(user);
        return "valid";
    }

    public Token resendVerificationToken(String token) {
        Token tokenData = tokenDao.findByToken(token);
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, 30);

        tokenData.setExpiryDate(new Date(cal.getTime().getTime()));
        tokenData.setToken(UUID.randomUUID().toString());

        tokenDao.save(tokenData);
        return tokenData;
    }

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public String verifyPasswordToken(String token) {
        PasswordToken tokenData = passwordTokenDao.findByToken(token);
        if (tokenData == null) {
            return "invalid";
        }
        User user = tokenData.getUser();
        Calendar cal = Calendar.getInstance();
        if (tokenData.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
            return "expired";
        }
        return "valid";
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordTokenDao.findByToken(token).getUser());
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }

    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public PasswordToken findTokenByUser(User user) {
        return passwordTokenDao.findByUserId(user.getId());
    }

    public void savePasswordToken(PasswordToken passwordToken) {
        passwordTokenDao.save(passwordToken);
    }

}
