package com.notificationservice.dto;

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
    private String type;
    private LocalDateTime timestamp;
}

