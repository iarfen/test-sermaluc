package com.coopeuchTest.controller;

import com.coopeuchTest.model.Task;
import com.coopeuchTest.dao.TasksDAO;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class TasksController {
    
    @Autowired
    private TasksDAO tasksDAO;

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return (List<Task>) tasksDAO.findAll();
    }
    
    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable Long taskId) throws ResponseStatusException {
        return tasksDAO.findById(taskId).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"); } );
    }

    @PostMapping("/tasks")
    void addTask(@RequestBody Task task) {
        tasksDAO.save(task);
    }
    
    @PutMapping("/tasks/{taskId}")
    void putTask(@RequestBody Task task,@PathVariable Long taskId) throws ResponseStatusException {
        Optional<Task> dbTask = tasksDAO.findById(taskId);
        if (dbTask.isPresent())
        {
            task.setId(taskId);
            tasksDAO.save(task);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }
    
    @DeleteMapping("/tasks/{taskId}")
    void deleteTask(@PathVariable Long taskId) throws ResponseStatusException {
        Optional<Task> dbTask = tasksDAO.findById(taskId);
        if (dbTask.isPresent())
        {
            tasksDAO.deleteById(taskId);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }
}
