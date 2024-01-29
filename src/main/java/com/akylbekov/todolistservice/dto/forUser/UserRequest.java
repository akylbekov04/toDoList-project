package com.akylbekov.todolistservice.dto.forUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.akylbekov.todolistservice.model.User}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest{
    Long id;
    String username;
    String email;
}