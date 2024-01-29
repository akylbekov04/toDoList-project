package com.akylbekov.todolistservice.controller;

import com.akylbekov.todolistservice.dto.forTask.TaskRequest;
import com.akylbekov.todolistservice.dto.forTask.TaskResponse;
import com.akylbekov.todolistservice.dto.forUser.UserRequest;
import com.akylbekov.todolistservice.dto.forUser.UserResponse;
import com.akylbekov.todolistservice.model.Task;
import com.akylbekov.todolistservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    @Autowired
    private final UserService userService;

    //метод для создания юзера с предоставленными данными
    @PostMapping("/createUser")
    @Operation(summary = "Create user")
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Data for User")
            @RequestBody UserRequest userRequest) {

        return userService.createUser(userRequest);
    }

    //метод для нахождения юзера с предоставленным ID
    @GetMapping("/getUser/{id}")
    @Operation(summary = "Get User by his ID")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "Id of User")
            @PathVariable Long id) {

        return userService.findUserById(id);
    }

    //метод для получения всех юзеров
    @GetMapping("/getAllUsers")
    @Operation(summary = "Get all Users")
    public List<UserResponse> getAllUsers() {

        return userService.findAllUsers();
    }

    //метод для удаления определенно юзера по его ID
    @DeleteMapping("/deleteUser/{id}")
    @Operation(summary = "Delete User by his ID")
    public ResponseEntity<String> deleteUserById(@Parameter(description = "Id of User") @PathVariable Long id) {

        return userService.deleteById(id);
    }

    //метод для обновления данных юзера по его ID
    @PutMapping("/updateUser/{id}")
    @Operation(summary = "Update User by his ID")
    public ResponseEntity<UserResponse> updateUserById(
            @Parameter(description = "Id of User") @PathVariable Long id,
            @Parameter(description = "Data for Task") @RequestBody UserRequest userRequest) {

        return userService.updateUserById(id, userRequest);
    }
}
