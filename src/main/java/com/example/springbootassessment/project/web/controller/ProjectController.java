package com.example.springbootassessment.project.web.controller;

import com.example.springbootassessment.project.service.ProjectService;
import com.example.springbootassessment.project.web.dto.ProjectDto;
import com.example.springbootassessment.project.web.mapper.ProjectMapper;
import com.example.springbootassessment.task.web.dto.TaskDto;
import com.example.springbootassessment.task.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper; // For mapping tasks in getProjectTasks

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        var projects = projectService.findAll();
        return ResponseEntity.ok(projectMapper.toDtoList(projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        var project = projectService.findById(id);
        return ResponseEntity.ok(projectMapper.toDto(project));
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        var projectDomain = projectMapper.toDomain(projectDto);
        // Similar to TaskController, ID from DTO should be ignored for new projects.
        // Domain object's constructor or toDomain method should handle this.
        // Service's save method handles new entity ID generation.
        var savedProject = projectService.save(projectDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.toDto(savedProject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        var projectDomain = projectMapper.toDomain(projectDto);
        var updatedProject = projectService.update(id, projectDomain);
        return ResponseEntity.ok(projectMapper.toDto(updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getProjectTasks(@PathVariable Long id) {
        var tasks = projectService.findTasksByProjectId(id);
        // The tasks returned from projectService are domain objects, so we use taskMapper
        return ResponseEntity.ok(taskMapper.mapToDtoList(tasks));
    }
}
