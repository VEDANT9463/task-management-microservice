package com.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    private Long taskId;
    private Long userId;
    private String title;
    private String description;
    private String type; // TASK_CREATED / TASK_DELETED
    private LocalDateTime timestamp;
}

