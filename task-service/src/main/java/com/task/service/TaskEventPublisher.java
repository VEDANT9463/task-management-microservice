package com.task.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.dto.TaskEvent;
import com.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "task-events";

    public void publish(Task task, String type) {
        try {
            TaskEvent event = new TaskEvent(
                    task.getId(),
                    task.getUserId(), // updated from task.getUser().getId()
                    task.getTitle(),
                    task.getDescription(),
                    type,
                    LocalDateTime.now()
            );

            kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


