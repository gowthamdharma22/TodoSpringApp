package com.gd.todo.service;

import com.gd.todo.exception.BadRequestException;
import com.gd.todo.exception.TodoNotFoundException;
import com.gd.todo.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.gd.todo.dao.TodoDao;

@Service
public class TodoService {

    private TodoDao todoDao;

    @Autowired
    public void setTodoDao(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    public List<Todo> getAllTodoService() {
        try {
            return todoDao.findAll();
        } catch (Exception e) {
            throw new BadRequestException("Error while getting all data", e);
        }
    }

    public void addTodoService(Todo todoBody) {
        try {
            todoDao.save(todoBody);
        } catch (Exception e) {
            throw new BadRequestException("Error in adding data", e);
        }
    }

    public void updateTodoService(Integer id, Todo todoBody) {
        try {
            Optional<Todo> optionalTodo = todoDao.findById(id);
            if (optionalTodo.isPresent()) {
                Todo todo = optionalTodo.get();
                todo.setContent(todoBody.getContent());
                todo.setCompleted(todoBody.getCompleted());
                todoDao.save(todo);
            } else {
                throw new TodoNotFoundException("Todo with id " + id + " not found");
            }
        } catch (TodoNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error in updating the todo", e);
        }
    }

    public void deleteTodoService(Integer id) {
        try {
            Optional<Todo> optionalTodo = todoDao.findById(id);
            if (optionalTodo.isPresent()) {
                todoDao.deleteById(id);
            } else {
                throw new TodoNotFoundException("Todo with id " + id + " not found");
            }
        } catch (TodoNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error in deleting the todo", e);
        }
    }
}
