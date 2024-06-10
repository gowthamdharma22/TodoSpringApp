package com.gd.todo.dao;

import com.gd.todo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
