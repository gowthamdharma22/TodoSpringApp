package com.gd.todo.controller;

import com.gd.todo.dao.TokenDao;
import com.gd.todo.dao.UserDao;
import com.gd.todo.entity.PasswordToken;
import com.gd.todo.entity.Token;
import com.gd.todo.entity.User;
import com.gd.todo.event.RegistrationCompleteEvent;
import com.gd.todo.event.ResendResgistrationCompleteEvent;
import com.gd.todo.model.PasswordModel;
import com.gd.todo.model.UserModel;
import com.gd.todo.responseEntity.ResponseHandler;
import com.gd.todo.service.CustomUserDetailService;
import com.gd.todo.service.UserService;
import com.gd.todo.util.JWTUtil;
import com.gd.todo.util.VerificationMailSender;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ResponseHandler responseHandler;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private VerificationMailSender mailSender;

    @GetMapping("/")
    public String hello() {
        return "WELCOME TO TODO BACKEND";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserModel userModel, final HttpServletRequest request) {
        try {
            User user = userService.registUser(userModel);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            return responseHandler.responseBuilder("Verification Link sent Successfully", HttpStatus.CREATED, null);
        } catch (Exception e) {
            return responseHandler.responseBuilder("Registration Link Failed", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<Object> verifyRegistration(@RequestParam String token) {
        try {
            String res = userService.verifyToken(token);
            if (res.equalsIgnoreCase("valid")) {
                return responseHandler.responseBuilder("User Verified Successfully", HttpStatus.OK, null);
            } else if (res.equalsIgnoreCase("expired")) {
                return responseHandler.responseBuilder("User has been Expired", HttpStatus.NOT_FOUND, null);
            }
            return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
        }

    }

    @GetMapping("/resendVerification")
    public ResponseEntity<Object> resendVerification(@RequestParam String token, HttpServletRequest request) {
        try {
            Token tokenData = userService.resendVerificationToken(token);
            User user = tokenData.getUser();
            publisher.publishEvent(new ResendResgistrationCompleteEvent(user, tokenData.getToken(), applicationUrl(request)));
            return responseHandler.responseBuilder("User Resend Verification Successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return responseHandler.responseBuilder("User Resend Verification Failed", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request) {
        try {
            User user = userService.findUserByEmail(passwordModel.getEmail());
            if (user == null) {
                return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
            }
            String token = UUID.randomUUID().toString();
            PasswordToken passwordToken = userService.findTokenByUser(user);
            if (passwordToken == null) {
                passwordToken = new PasswordToken(user, token);
            } else {
                passwordToken.setToken(token);
                passwordToken.setExpiryDate(passwordToken.calculateExpiryDate());
            }
            userService.savePasswordToken(passwordToken);
            String url = resetPasswordUrl(user, token, applicationUrl(request));
            return responseHandler.responseBuilder("Reset Verification sent Successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
        }
    }


    @PostMapping("/verifyPasswordReset")
    public ResponseEntity<Object> verifyPasswordReset(@RequestParam String token, @RequestBody PasswordModel passwordModel) {
        String res = userService.verifyPasswordToken(token);
        if (!res.equalsIgnoreCase("valid")) {
            return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return responseHandler.responseBuilder("Password Reset Successfully", HttpStatus.OK, null);
        } else {
            return responseHandler.responseBuilder("Invalid Token", HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody PasswordModel passwordModel) {
        try {
            User user = userService.findUserByEmail(passwordModel.getEmail());
            if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
                return responseHandler.responseBuilder("Invalid Old Password", HttpStatus.BAD_REQUEST, null);
            }
            userService.changePassword(user, passwordModel.getNewPassword());
            return responseHandler.responseBuilder("Password Changed Successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            return responseHandler.responseBuilder("Something went wrong", HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginWithJWT(@RequestBody UserModel userModel) {
        try {
            System.out.println(userModel.toString() + "CHECK");
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));
            } catch (BadCredentialsException e) {
                return responseHandler.responseBuilder("Invalid Credentials", HttpStatus.BAD_REQUEST, null);
            }

            final UserDetails userDetails = customUserDetailService.loadUserByUsername(userModel.getEmail());
            final String jwtToken = jwtUtil.generateToken(userDetails);
            return responseHandler.responseBuilder("Successfully logged in", HttpStatus.OK, jwtToken);
        } catch (Exception e) {
            return responseHandler.responseBuilder("Something went wrong", HttpStatus.BAD_REQUEST, null);
        }
    }


    private String resetPasswordUrl(User user, String token, String applicationUrl) {
        String url = applicationUrl
                + "/verifyPasswordReset?token="
                + token;

        //log instead of sendingMail
        mailSender.sendMail(user.getEmail(), "Reset Password Link", "Click the url to reset your Password : \n" + url);
        log.info("Click the url to reset your Password : {}", url);

        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }


}
