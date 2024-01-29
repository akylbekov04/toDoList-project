package com.akylbekov.todolistservice.dto.forTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.akylbekov.todolistservice.model.Task}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskResponse{
    Long id;
    String description;
    Boolean done;
}