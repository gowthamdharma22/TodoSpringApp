package com.gd.todo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
