package com.notificationservice.service;

import com.notificationservice.dto.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTaskCreatedEmail(TaskEvent event) {
        sendEmail(
                getUserEmail(event.getUserId()),
                "Task Created: " + event.getTitle(),
                "Task Details:\n" +
                        "Title: " + event.getTitle() + "\n" +
                        "Description: " + event.getDescription() + "\n" +
                        "Created at: " + event.getTimestamp()
        );
    }

    public void sendTaskDeletedEmail(TaskEvent event) {
        sendEmail(
                getUserEmail(event.getUserId()),
                "Task Deleted: " + event.getTitle(),
                "The task has been deleted.\n" +
                        "Title: " + event.getTitle() + "\n" +
                        "Deleted at: " + event.getTimestamp()
        );
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    // Replace this with actual DB/user lookup
    private String getUserEmail(Long userId) {
        return "user" + userId + "@example.com";
    }
}
