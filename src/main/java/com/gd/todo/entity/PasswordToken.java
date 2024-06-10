package com.gd.todo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(name = "password_token")
@NoArgsConstructor
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expiryDate;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "FK_PASSWORD_VERIFY_TOKEN"))
    private User user;
    private final static Integer EXPIRES_IN = 30;

    public PasswordToken(User user, String token) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    public PasswordToken(User user) {
        super();
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    public Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, PasswordToken.EXPIRES_IN);
        return new Date(cal.getTime().getTime());
    }
}
