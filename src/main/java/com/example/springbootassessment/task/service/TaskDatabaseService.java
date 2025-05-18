package com.example.springbootassessment.task.service;

import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import com.example.springbootassessment.project.repository.ProjectRepository;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.domain.mapper.TaskEntityMapper;
import com.example.springbootassessment.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskDatabaseService implements TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskEntityMapper taskEntityMapper;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskEntityMapper::map)
                .toList();
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        return projectRepository
                .findById(projectId)
                .map(i -> i.getTasks().stream().map(taskEntityMapper::map).toList())
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }
}
