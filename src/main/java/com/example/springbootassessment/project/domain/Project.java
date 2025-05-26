package com.example.springbootassessment.project.domain;

import com.example.springbootassessment.task.domain.Task;

import java.util.HashSet;
import java.util.Set;

public record Project(
    long id,
    String name,
    String description,
    Set<Task> tasks
) {
    public Project {
        if (tasks == null) {
            tasks = new HashSet<>();
        }
    }
}
