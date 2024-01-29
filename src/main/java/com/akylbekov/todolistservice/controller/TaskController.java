package com.akylbekov.todolistservice.controller;

import com.akylbekov.todolistservice.dto.forTask.TaskRequest;
import com.akylbekov.todolistservice.dto.forTask.TaskResponse;
import com.akylbekov.todolistservice.model.Task;
import com.akylbekov.todolistservice.service.TaskService;
import com.akylbekov.todolistservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private final TaskService taskService;

    //метод для получения всех тасков юзера с предоставленным ID
    @GetMapping("/getTasksOfUser/{id}")
    @Operation(summary = "Get all Tasks of User with provided ID")
    List<Task> getTasksOfUserWithId(@Parameter(description = "Id of User")
                                    @PathVariable Long id) {

        return taskService.findAllTasksForUserWithId(id);
    }

    //метод для создания таска для юзера с предоставленным ID
    @PostMapping("/createTaskForUser/{id}")
    @Operation(summary = "Create new Task for User with provided ID")
    public ResponseEntity<TaskResponse> createTaskForUserWithId(
            @Parameter(description = "Id of User") @PathVariable Long id,
            @Parameter(description = "Data of Task") @RequestBody TaskRequest task) {

        return taskService.createTaskForUserWithId(id, task);
    }

    //метод для удаления таска юзера с предоставленными ID
    @DeleteMapping("/deleteTask/{idForUser}/{idForTask}")
    @Operation(summary = "Delete Task of User with provided ID's of user and task")
    public ResponseEntity<String> deleteTaskForUserWithId(
            @Parameter(description = "Id of User") @PathVariable Long idForUser,
            @Parameter(description = "Id of Task") @PathVariable Long idForTask) {

        return taskService.deleteTaskWithIdForUserWithId(idForUser, idForTask);
    }

    //метод для обновления значений таска юзера с предоставленными ID
    //и новыми данными для таска
    @PutMapping("/updateTask/{idForUser}/{idForTask}")
    @Operation(summary = "Update Task of User with provided ID's of user and task")
    public ResponseEntity<TaskResponse> updateTaskForUserWithId(
            @Parameter(description = "Id of User") @PathVariable Long idForUser,
            @Parameter(description = "Id of Task") @PathVariable Long idForTask,
            @Parameter(description = "Data of new Task") @RequestBody TaskRequest taskRequest) {

        return taskService.updateTaskWithIdForUserWithId(idForUser, idForTask, taskRequest);
    }
}
