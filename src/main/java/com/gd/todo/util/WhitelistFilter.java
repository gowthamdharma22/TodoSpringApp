package com.gd.todo.util;

import com.gd.todo.filters.JWTFilters;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class WhitelistFilter extends OncePerRequestFilter {

    private JWTFilters jwtFilters;
    private String[] whitelist;

    public WhitelistFilter(JWTFilters jwtFilters, String[] whitelist) {
        this.jwtFilters = jwtFilters;
        this.whitelist = whitelist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println(path + "PATH");
        System.out.println(isWhitelisted(path) + "WHITE");
        System.out.println(Arrays.toString(this.whitelist) + "WHITELUST");
        if (isWhitelisted(path)) {
            filterChain.doFilter(request, response);
        } else {
            jwtFilters.doFilter(request, response, filterChain);
        }
    }

    private boolean isWhitelisted(String path) {
        return Arrays.asList(this.whitelist).contains(path);
    }

}
