package com.akylbekov.todolistservice.dto.forUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.akylbekov.todolistservice.model.User}
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse{
    Long id;
    String username;
    String email;
}