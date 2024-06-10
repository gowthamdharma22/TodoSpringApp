package com.gd.todo.dao;

import com.gd.todo.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenDao extends JpaRepository<PasswordToken, Integer> {
    
    PasswordToken findByToken(String token);

    PasswordToken findByUserId(Long id);
}
