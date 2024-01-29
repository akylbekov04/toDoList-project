package com.akylbekov.todolistservice;

import com.akylbekov.todolistservice.dto.forTask.TaskRequest;
import com.akylbekov.todolistservice.dto.forTask.TaskResponse;
import com.akylbekov.todolistservice.model.Task;
import com.akylbekov.todolistservice.model.User;
import com.akylbekov.todolistservice.service.TaskService;
import com.akylbekov.todolistservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.akylbekov.todolistservice.UserControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TaskControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private TaskService taskService;
    @Autowired
    private MockMvc mockMvc;

    //тест для проверки http запроса для получения таска юзера
    @Test
    void testGetTasksOfUser() throws Exception {
        Long userId = 1L;
        Task task = Task.builder()
                .id(1L)
                .description("task1")
                .done(false)
                .build();
        List<Task> mockTasks = List.of(task);
        when(taskService.findAllTasksForUserWithId(userId)).thenReturn(mockTasks);

        mockMvc.perform(get("http://localhost:8071/api/task/getTasksOfUser/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("task1")))
                .andExpect(jsonPath("$[0].done", is(false)));
    }
    //тест для проверки http запроса на создания таска для юзера
    @Test
    void testCreateTaskForUser() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .description("description")
                .done(false)
                .build();
        TaskResponse taskResponse = TaskResponse.builder()
                .id(1L)
                .description("description")
                .done(false)
                .build();
        when(taskService.createTaskForUserWithId(any(), eq(taskRequest)))
                .thenReturn(new ResponseEntity<>(taskResponse, HttpStatus.CREATED));

        mockMvc.perform(post("http://localhost:8071/api/task/createTaskForUser/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.done", is(false)));
    }
    //тест для проверки http запроса на удаления таска юзера
    @Test
    void testDeleteTaskOfUser() throws Exception {
        when(taskService.deleteTaskWithIdForUserWithId(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>
                        ("Task with id 3 was deleted from user with id 1", HttpStatus.OK));
        mockMvc.perform(delete("http://localhost:8071/api/task/deleteTask/1/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Task with id 3 was deleted from user with id 1"));
    }
    //тест для проверки http запроса для обновления таска юзера
    @Test
    void testUpdateTaskOfUser()throws Exception{
        TaskRequest taskRequest = TaskRequest.builder()
                .description("description")
                .done(true)
                .build();
        TaskResponse expectedTaskResponse = TaskResponse.builder()
                .id(2L)
                .description("description")
                .done(true)
                .build();
        when(taskService.updateTaskWithIdForUserWithId(anyLong(),anyLong(),eq(taskRequest)))
                .thenReturn(new ResponseEntity<>(expectedTaskResponse,HttpStatus.OK));

        mockMvc.perform(put("http://localhost:8071/api/task/updateTask/1/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id",is(2)))
                .andExpect(jsonPath("$.description",is("description")))
                .andExpect(jsonPath("$.done",is(true)));
    }

}
