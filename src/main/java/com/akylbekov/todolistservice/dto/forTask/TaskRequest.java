package com.akylbekov.todolistservice.dto.forTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.akylbekov.todolistservice.model.Task}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest{
    String description;
    Boolean done;
}