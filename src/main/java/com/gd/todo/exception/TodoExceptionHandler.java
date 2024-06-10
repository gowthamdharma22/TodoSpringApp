package com.gd.todo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TodoExceptionHandler {

    @ExceptionHandler(value = {TodoNotFoundException.class})
    public ResponseEntity<Object> handleTodoNotFound(TodoNotFoundException todoNotFoundException) {
        TodoException e = new TodoException(todoNotFoundException.getMessage(), todoNotFoundException.getCause(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException badRequestException) {
        TodoException e = new TodoException(badRequestException.getMessage(), badRequestException.getCause(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
}
