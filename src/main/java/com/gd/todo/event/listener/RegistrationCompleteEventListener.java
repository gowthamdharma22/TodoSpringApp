package com.gd.todo.event.listener;

import com.gd.todo.entity.User;
import com.gd.todo.event.RegistrationCompleteEvent;
import com.gd.todo.service.UserService;
import com.gd.todo.util.VerificationMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        //To create a verification Token
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveToken(user, token);

        //Send mail to user
        String url = event.getApplicationUrl()
                + "/verifyRegistration?token="
                + token;

        //log instead of sendingMail
        mailSender.sendMail(event.getUser().getEmail(), "Account Verification Mail", "Click the url to verify your Account : \n" + url);
        log.info("Click the url to verify your Account : {}", url);
    }
}
