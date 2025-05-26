package com.example.springbootassessment.task.service;

import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import com.example.springbootassessment.project.service.port.ProjectRepositoryPort;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.domain.exception.TaskNotFoundException;
import com.example.springbootassessment.task.service.port.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskDatabaseService implements TaskService {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;

    @Override
    public List<Task> findAll() {
        return taskRepositoryPort.findAll();
    }

    @Override
    public Task findById(Long id) throws TaskNotFoundException {
        return taskRepositoryPort.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Override
    public Task save(Task task) {
        // Assuming TaskMapper handles projectId if present in Task domain (currently not)
        // Or that tasks are created independently of projects here.
        // Further validation (e.g., task.dueDate()) can be added here if needed.
        return taskRepositoryPort.save(task);
    }

    @Override
    public void deleteById(Long id) throws TaskNotFoundException {
        if (!taskRepositoryPort.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepositoryPort.deleteById(id);
    }

    @Override
    public Task update(Long id, Task task) throws TaskNotFoundException {
        if (!taskRepositoryPort.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        // Ensure the task to be saved has the correct ID from the path parameter
        Task taskToUpdate = new Task(id, task.title(), task.description(), task.completed(), task.dueDate());
        return taskRepositoryPort.save(taskToUpdate);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) throws ProjectNotFoundException {
        if (!projectRepositoryPort.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
        return taskRepositoryPort.findAllByProjectId(projectId);
    }
}
