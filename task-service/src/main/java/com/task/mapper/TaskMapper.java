package com.task.mapper;


import com.task.dto.CreateTaskRequest;
import com.task.dto.TaskResponse;
import com.task.dto.UpdateTaskRequest;
import com.task.entity.Task;

public class TaskMapper {

    // ✅ Convert request → entity (using userId instead of User object)
    public static Task toEntity(CreateTaskRequest req, Long userId) {
        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setCompleted(false);
        task.setDeleted(false);
        task.setUserId(userId); // ✅ store only userId
        return task;
    }

    // ✅ Convert entity → response
    public static TaskResponse toResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        res.setCompleted(task.isCompleted());
        res.setCreatedAt(task.getCreatedAt());
        res.setUpdatedAt(task.getUpdatedAt());
        res.setCompletedAt(task.getCompletedAt());// new field
        return res;
    }

    // ✅ Update entity safely
    public static void updateEntity(Task task, UpdateTaskRequest req) {
        if (req.getTitle() != null) task.setTitle(req.getTitle());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getCompleted() != null) task.setCompleted(req.getCompleted());
    }
}


