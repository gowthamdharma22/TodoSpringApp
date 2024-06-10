package com.gd.todo.event.listener;

import com.gd.todo.event.ResendResgistrationCompleteEvent;
import com.gd.todo.service.UserService;
import com.gd.todo.util.VerificationMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ResendRegistrationCompleteEventListener implements ApplicationListener<ResendResgistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationMailSender mailSender;

    @Override
    public void onApplicationEvent(ResendResgistrationCompleteEvent event) {

        String url = event.getApplicationUrl()
                + "/verifyRegistration?token="
                + event.getToken();

        //log instead of sendingMail
        mailSender.sendMail(event.getUser().getEmail(), "Account Verification Mail", "Click the url to verify your Account : \n" + url);
    }
}
