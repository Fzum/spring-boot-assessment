package com.example.springbootassessment.project.web.dto;

import com.example.springbootassessment.task.web.dto.TaskDto;
import java.util.Set;

public record ProjectDto(
    Long id,
    String name,
    String description,
    Set<TaskDto> tasks
) {}
