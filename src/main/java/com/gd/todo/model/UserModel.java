package com.gd.todo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    private String matchingPassword;

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                '}';
    }
}
