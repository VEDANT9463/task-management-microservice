package com.task.controller;

import com.task.dto.CreateTaskRequest;
import com.task.dto.TaskResponse;
import com.task.dto.UpdateTaskRequest;
import com.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // ✅ Extract logged-in user's email from JWT
    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // CREATE TASK
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request, getCurrentUserEmail());
    }

    // GET ALL TASKS (with pagination + optional completed filter)
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<TaskResponse> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) Boolean completed
    ) {
        return taskService.getTasks(getCurrentUserEmail(), page, size, sortBy, completed);
    }

    // SEARCH TASKS
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public Page<TaskResponse> searchTasks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        return taskService.searchTasks(getCurrentUserEmail(), keyword, page, size, sortBy);
    }

    // UPDATE TASK
    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('USER')")
    public TaskResponse updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return taskService.updateTask(taskId, request, getCurrentUserEmail());
    }

    // DELETE TASK (Soft delete)
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId, getCurrentUserEmail());
    }

    // ADMIN: GET ALL TASKS
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskResponse> getAllTasksForAdmin() {
        return taskService.getAllTasksAdmin();
    }
}


