package com.task.service;

import com.task.dto.CreateTaskRequest;
import com.task.dto.TaskResponse;
import com.task.dto.UpdateTaskRequest;
import com.task.entity.Task;
import lombok.RequiredArgsConstructor;
import com.task.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.task.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final WebClient webClient;

    // ✅ Resolve userId using user-service via email
    private Long resolveUserId(String email) {
        return webClient.get()
                .uri("http://user-service:8082/api/users/by-email/{email}", email)
                .retrieve()
                .bodyToMono(UserDto.class)
                .map(UserDto::getId)
                .block();
    }

    // ✅ CREATE TASK
    public TaskResponse createTask(CreateTaskRequest request, String email) {
        Long userId = resolveUserId(email);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .userId(userId)
                .completed(false)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    // ✅ GET ALL TASKS
    public Page<TaskResponse> getTasks(String email, int page, int size, String sortBy, Boolean completed) {
        Long userId = resolveUserId(email);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<Task> tasks;

        if (completed == null) {
            tasks = taskRepository.findByUserIdAndDeletedFalse(userId, pageable);
        } else {
            tasks = taskRepository.findByUserIdAndCompletedAndDeletedFalse(userId, completed, pageable);
        }

        return tasks.map(this::mapToResponse);
    }

    // ✅ SEARCH TASKS
    public Page<TaskResponse> searchTasks(String email, String keyword, int page, int size, String sortBy) {
        Long userId = resolveUserId(email);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return taskRepository.searchTasks(userId, keyword, pageable)
                .map(this::mapToResponse);
    }

    // ✅ UPDATE TASK
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest req, String email) {
        Long userId = resolveUserId(email);

        Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")
                );

        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setCompleted(req.getCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(taskRepository.save(task));
    }

    // ✅ DELETE TASK (Soft delete)
    public void deleteTask(Long taskId, String email) {
        Long userId = resolveUserId(email);

        Task task = taskRepository.findByIdAndDeletedFalse(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")
                );

        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        task.setDeleted(true);
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    // ✅ ADMIN ACCESS
    public List<TaskResponse> getAllTasksAdmin() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Mapper
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}

























