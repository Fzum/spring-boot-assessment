package com.example.springbootassessment.task.adapter.persistence;

import com.example.springbootassessment.task.adapter.persistence.mapper.TaskMapper;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.repository.TaskRepository;
import com.example.springbootassessment.task.service.port.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return taskMapper.toDomainList(taskRepository.findAll());
    }

    @Override
    public Task save(Task task) {
        var taskEntity = taskMapper.toEntity(task);
        var savedEntity = taskRepository.save(taskEntity);
        return taskMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        return taskMapper.toDomainList(taskRepository.findByProjectId(projectId));
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}
