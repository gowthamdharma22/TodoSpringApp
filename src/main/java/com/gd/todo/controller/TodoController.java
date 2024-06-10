package com.gd.todo.controller;

import com.gd.todo.exception.BadRequestException;
import com.gd.todo.entity.Todo;
import com.gd.todo.responseEntity.ResponseHandler;
import com.gd.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("todo")
public class TodoController {

    private TodoService todoService;
    private ResponseHandler responseHandler;

    @Autowired
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Autowired
    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @GetMapping("getAll")
    public ResponseEntity<Object> getAllTodo() {
        try {
            List<Todo> todos = todoService.getAllTodoService();
            return responseHandler.responseBuilder("Here are the results", HttpStatus.OK, todos);
        } catch (Exception e) {
            throw new BadRequestException("Error in fetching the todo", e);
        }
    }

    @PostMapping("add")
    public ResponseEntity<Object> addTodo(@RequestBody Todo todoBody) {
        try {
            todoService.addTodoService(todoBody);
            return responseHandler.responseBuilder("Todo is added successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            throw new BadRequestException("Error in adding the todo", e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateTodo(@PathVariable Integer id, @RequestBody Todo todoBody) {
        try {
            todoService.updateTodoService(id, todoBody);
            return responseHandler.responseBuilder("Todo is updated successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            throw new BadRequestException("Error in updating the todo", e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTodo(@PathVariable Integer id) {
        try {
            todoService.deleteTodoService(id);
            return responseHandler.responseBuilder("Todo is deleted successfully", HttpStatus.OK, null);
        } catch (Exception e) {
            throw new BadRequestException("Error in deleting the todo", e);
        }
    }
}
