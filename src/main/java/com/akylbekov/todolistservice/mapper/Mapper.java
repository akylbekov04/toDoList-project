package com.akylbekov.todolistservice.mapper;

import com.akylbekov.todolistservice.dto.forTask.TaskResponse;
import com.akylbekov.todolistservice.dto.forUser.UserResponse;
import com.akylbekov.todolistservice.model.Task;
import com.akylbekov.todolistservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Mapper {
    //класс для конвертации моделей в респонс классы
    @Autowired
    private final Task task;

    public TaskResponse mapToTaskResponse(Task task){
        TaskResponse taskResponse = TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .done(task.getDone())
                .build();
        return taskResponse;
    }

    public UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
