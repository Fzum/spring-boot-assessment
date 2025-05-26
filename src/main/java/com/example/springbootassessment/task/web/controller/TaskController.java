package com.example.springbootassessment.task.web.controller;

import com.example.springbootassessment.task.service.TaskService;
import com.example.springbootassessment.task.web.dto.TaskDto;
import com.example.springbootassessment.task.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        var tasks = taskService.findAll();
        return ResponseEntity.ok(taskMapper.mapToDtoList(tasks));
    }

    // Note: The original subtask description for TaskController had getTasksByProjectId here.
    // However, a more RESTful approach for getting tasks of a specific project
    // is usually /projects/{projectId}/tasks, which will be in ProjectController.
    // For now, I'll keep the /tasks/{id} as getTaskById.
    // If /tasks/{projectId} was indeed intended for tasks filtered by project ID,
    // the method name getTasksByProjectId should be findAllByProjectId from the service.
    // I will assume /tasks/{id} for individual task fetching for now.
    // The subtask also explicitly asks for getTasksByProjectId in ProjectController later.

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok(taskMapper.map(task));
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        var taskDomain = taskMapper.toDomain(taskDto);
        // The ID from the DTO (if any) should be ignored, as it's a new task.
        // For a record, this means creating a new one without the ID if the toDomain method doesn't already handle it.
        // Assuming Task domain object's constructor or toDomain handles ID appropriately for creation.
        // If Task DTO can have an ID, and toDomain uses it, we might need a specific toDomainForCreate method.
        // For now, assuming service's save method handles new entity ID generation.
        var savedTask = taskService.save(taskDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.map(savedTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        var taskDomain = taskMapper.toDomain(taskDto);
        var updatedTask = taskService.update(id, taskDomain);
        return ResponseEntity.ok(taskMapper.map(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // This endpoint is to specifically address the getTasksByProjectId from the original TaskController requirements
    // if it was meant to be separate from ProjectController's /projects/{projectId}/tasks
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(@PathVariable Long projectId) {
        var tasks = taskService.findAllByProjectId(projectId);
        return ResponseEntity.ok(taskMapper.mapToDtoList(tasks));
    }
}
