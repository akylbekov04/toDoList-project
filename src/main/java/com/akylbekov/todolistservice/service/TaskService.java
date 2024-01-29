package com.akylbekov.todolistservice.service;

import com.akylbekov.todolistservice.dto.forTask.TaskRequest;
import com.akylbekov.todolistservice.dto.forTask.TaskResponse;
import com.akylbekov.todolistservice.mapper.Mapper;
import com.akylbekov.todolistservice.model.Task;
import com.akylbekov.todolistservice.model.User;
import com.akylbekov.todolistservice.repository.TaskRepository;
import com.akylbekov.todolistservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final Mapper mapper;

    @Transactional(readOnly = true)
    public List<Task> findAllTasksForUserWithId(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        //проверка на наличие юзера в базе данных
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Task> tasks = user.getTasks();
            log.info("Tasks for user with id: {} were found.", id);
            return tasks;
        } else {
            throw new RuntimeException("User with id: "+id+" was not found, please try another id");
        }
    }

    @Transactional
    public ResponseEntity<TaskResponse> createTaskForUserWithId(Long id,
                                                                TaskRequest taskRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        //проверка на наличие юзера в базе данных
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Task task = Task.builder()
                    .description(taskRequest.getDescription())
                    .done(taskRequest.getDone())
                    .user(user)
                    .build();
            List<Task> userTasks = user.getTasks();
            userTasks.add(task);
            userRepository.save(user);
            taskRepository.save(task);
            TaskResponse taskResponse = mapper.mapToTaskResponse(task);
            return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteTaskWithIdForUserWithId(Long idForUser, Long idForTask) {
        Optional<User> userOptional = userRepository.findById(idForUser);
        //проверка на наличие юзера в базе данных
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Task> taskOptional = user.getTasks().stream()
                    .filter(task -> task.getId().equals(idForTask))
                    .findFirst();
            //проверка на наличие таска в базе данных
            if (taskOptional.isPresent()) {
                user.getTasks().remove(taskOptional.get());
                taskRepository.deleteById(idForTask);
                userRepository.save(user);
                log.info("Task with id {} for user with id {} was deleted", idForTask, idForUser);
                return new ResponseEntity<>("Task with id " + idForTask + " for user with id " + idForUser + " was deleted",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Task with id " + idForTask + " was not found for user with id " + idForUser,
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User with id " + idForUser + " was not found",
                    HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<TaskResponse> updateTaskWithIdForUserWithId(Long idForUser,
                                                                      Long idForTask,
                                                                      TaskRequest taskRequest) {
        Optional<User> userOptional = userRepository.findById(idForUser);
        //проверка на наличие юзера в базе данных
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Task> userTasks = user.getTasks();
            int idForTaskIndex = idForTask.intValue() - 1; //преобразуем idForTask в индекс списка

            //проверяем, что индекс находится в допустимых пределах
            if (idForTaskIndex >= 0 && idForTaskIndex < userTasks.size()) {
                Task taskForUpdate = userTasks.get(idForTaskIndex);
                taskForUpdate.setDescription(taskRequest.getDescription());
                taskForUpdate.setDone(taskRequest.getDone());

                userRepository.save(user);
                taskRepository.save(taskForUpdate);
                log.info("Task with id: {} successfully updated",idForTask);
                return new ResponseEntity<>(mapper.mapToTaskResponse(taskForUpdate), HttpStatus.OK);
            } else {
                log.error("Task with id: {} for user with id: {} was not found", idForTask, idForUser);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            log.error("User with id: {} was not found", idForUser);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
