package com.akylbekov.todolistservice.service;

import com.akylbekov.todolistservice.dto.forUser.UserRequest;
import com.akylbekov.todolistservice.dto.forUser.UserResponse;
import com.akylbekov.todolistservice.mapper.Mapper;
import com.akylbekov.todolistservice.model.User;
import com.akylbekov.todolistservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final Mapper mapper;

    public ResponseEntity<UserResponse> createUser( UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .build();
        userRepository.save(user);
        log.info("User with id: {} was saved to database.", user.getId());

        UserResponse userResponse = mapper.mapToUserResponse(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<UserResponse> findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        //проверяем, что юзер находится в базе данных
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            UserResponse userResponse = mapper.mapToUserResponse(user);
            log.info("User with id: {} was found.",id);
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        } else {
            log.info("User with id: {} was not found, please try another id", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<UserResponse> findAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            return users.stream().map(mapper::mapToUserResponse).toList();
        } catch (Exception e){
            log.error("Sorry, internal server problems. Please try later.", e);
            throw new RuntimeException("Internal server error.", e);
        }
    }


    public ResponseEntity<String> deleteById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        //проверяем, что юзер находится в базе данных
        if(optionalUser.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>("User with id: "+id+" was deleted",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with id: "+id+" was not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<UserResponse> updateUserById(Long id, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        //проверяем, что юзер находится в базе данных
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
            UserResponse userResponse = mapper.mapToUserResponse(user);

            log.info("User with id: {} was updated.", id);
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
