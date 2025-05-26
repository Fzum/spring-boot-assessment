package com.example.springbootassessment.project.service;

import com.example.springbootassessment.project.domain.Project;
import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import com.example.springbootassessment.project.service.port.ProjectRepositoryPort;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.service.port.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService implements ProjectService {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public List<Project> findAll() {
        return projectRepositoryPort.findAll();
    }

    @Override
    public Project findById(Long id) throws ProjectNotFoundException {
        return projectRepositoryPort.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }

    @Override
    public Project save(Project project) {
        // Additional business logic for saving a project can be added here if needed.
        // For example, validating project name, description, etc.
        // The current Project domain object's compact constructor initializes tasks to an empty set.
        return projectRepositoryPort.save(project);
    }

    @Override
    public void deleteById(Long id) throws ProjectNotFoundException {
        if (!projectRepositoryPort.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
        // Note: orphanRemoval=true in ProjectEntity should handle deleting associated tasks.
        // If not, or if more specific logic is needed (e.g., soft delete tasks),
        // it would be handled here or in the persistence adapter.
        projectRepositoryPort.deleteById(id);
    }

    @Override
    public Project update(Long id, Project project) throws ProjectNotFoundException {
        if (!projectRepositoryPort.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
        // Ensure the project to be saved has the correct ID from the path parameter
        // and preserves existing tasks if they are not part of the update payload directly.
        // The current Project record will replace tasks if `project.tasks()` is different.
        // If tasks are managed separately and shouldn't be overwritten by project update,
        // one might need to fetch the existing project and merge fields.
        // For now, we assume the input `project` contains the intended set of tasks.
        Project projectToUpdate = new Project(id, project.name(), project.description(), project.tasks());
        return projectRepositoryPort.save(projectToUpdate);
    }

    @Override
    public List<Task> findTasksByProjectId(Long projectId) throws ProjectNotFoundException {
        if (!projectRepositoryPort.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
        return taskRepositoryPort.findAllByProjectId(projectId);
    }
}
