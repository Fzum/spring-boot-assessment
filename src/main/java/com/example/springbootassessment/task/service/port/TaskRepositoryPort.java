package com.example.springbootassessment.task.service.port;

import com.example.springbootassessment.task.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    Optional<Task> findById(Long id);
    List<Task> findAll();
    Task save(Task task);
    void deleteById(Long id);
    List<Task> findAllByProjectId(Long projectId);
    boolean existsById(Long id);
}
