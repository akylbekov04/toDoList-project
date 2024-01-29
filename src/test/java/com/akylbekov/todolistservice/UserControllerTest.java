package com.akylbekov.todolistservice;

import com.akylbekov.todolistservice.dto.forUser.UserRequest;
import com.akylbekov.todolistservice.dto.forUser.UserResponse;
import com.akylbekov.todolistservice.model.User;
import com.akylbekov.todolistservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    //тест для проверки http запроса на получение юзера
    @Test
    void testGetUserById() throws Exception {
        UserResponse mockUser = UserResponse.builder()
                .id(1L)
                .username("akylbekov")
                .email("akylbekov@mail.ru")
                .build();
        when(userService.findUserById(1L))
                .thenReturn(new ResponseEntity<>(mockUser, HttpStatus.OK));

        mockMvc.perform(get("http://localhost:8071/api/user/getUser/{id}", 1))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("akylbekov")))
                .andExpect(jsonPath("$.email", is("akylbekov@mail.ru")));
    }
    //тест для проверки http запроса на создание юзера
    @Test
    void testCreateUser() throws Exception {
        UserResponse mockUser = UserResponse.builder()
                .id(1L)
                .username("akylbekov")
                .email("akylbekov@mail.ru")
                .build();
        UserRequest postUser = UserRequest.builder()
                .username("akylbekov")
                .email("akylbekov@mail.ru")
                .build();
        when(userService.createUser(any()))
                .thenReturn(new ResponseEntity<>(mockUser, HttpStatus.CREATED));

        mockMvc.perform(post("http://localhost:8071/api/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //для валидации возвращенных данных полей
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("akylbekov")))
                .andExpect(jsonPath("$.email", is("akylbekov@mail.ru")));
    }
    //тест для проверки http запроса на обновление юзера
    @Test
    void testUpdateUserById() throws Exception {
        //создаем объекты для теста
        UserRequest userRequest = UserRequest.builder()
                .username("akylbekov new")
                .email("new email")
                .build();
        UserResponse expectedUserResponse = new UserResponse(1L, "akylbekov new", "new email");

        //настройка поведения макета
        when(userService.updateUserById(anyLong(), any(UserRequest.class)))
                .thenReturn(new ResponseEntity<>(expectedUserResponse, HttpStatus.OK));
        //выполнение PUT запроса
        mockMvc.perform(put("http://localhost:8071/api/user/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.username",is("akylbekov new")))
                .andExpect(jsonPath("$.email",is("new email")));
    }
    //тест для проверки http запроса на удаление юзера
    @Test
    void testDeleteUserById() throws Exception {
        Long userId = 1L;
        when(userService.deleteById(anyLong()))
                .thenReturn(new ResponseEntity<>("User with id: " + userId + " was deleted", HttpStatus.OK));
        // Выполнение DELETE запроса
        mockMvc.perform(delete("http://localhost:8071/api/user/deleteUser/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("User with id: " + userId + " was deleted"));
    }
    //конвертация объекта в строку
    static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
