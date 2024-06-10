package com.gd.todo.responseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {

    public ResponseEntity<Object> responseBuilder(String message, HttpStatus status, Object responseObject) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(responseObject + "RES");
        if (responseObject != null) {
            response.put("data", responseObject);
        }
        response.put("status", status.value());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}
