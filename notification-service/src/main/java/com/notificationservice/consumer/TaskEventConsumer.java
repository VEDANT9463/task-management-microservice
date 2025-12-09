package com.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.dto.TaskEvent;
import com.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "task-events", groupId = "notification-group")
    public void consume(String message) {
        try {
            // Deserialize full TaskEvent
            TaskEvent event = objectMapper.readValue(message, TaskEvent.class);

            switch (event.getType()) {
                case "TASK_CREATED" -> emailService.sendTaskCreatedEmail(event);
                case "TASK_DELETED" -> emailService.sendTaskDeletedEmail(event);
                default -> System.out.println("Unknown event type: " + event.getType());
            }

        } catch (Exception e) {
            throw new RuntimeException("Kafka message processing failed", e);
        }
    }
}

