package com.example.springbootassessment.task.service;

import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.domain.exception.TaskNotFoundException;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task findById(Long id) throws TaskNotFoundException;

    Task save(Task task);

    void deleteById(Long id) throws TaskNotFoundException;

    Task update(Long id, Task task) throws TaskNotFoundException;

    /**
     * Returns all tasks for the given project ID.
     *
     * @param projectId the ID of the project
     * @return list of tasks belonging to the project
     * @throws com.example.springbootassessment.project.domain.exception.ProjectNotFoundException if the project with the given ID does not exist
     */
    List<Task> findAllByProjectId(Long projectId);
}
