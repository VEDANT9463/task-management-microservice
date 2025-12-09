package com.task.dto;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    private Boolean completed; // optional for partial updates
}

