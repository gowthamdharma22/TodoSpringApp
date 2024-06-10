package com.gd.todo.event;

import com.gd.todo.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResendResgistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String token;
    private String applicationUrl;

    public ResendResgistrationCompleteEvent(User user, String token, String applicationUrl) {
        super(user);
        this.user = user;
        this.token = token;
        this.applicationUrl = applicationUrl;
    }
}
