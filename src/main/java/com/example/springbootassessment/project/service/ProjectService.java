package com.example.springbootassessment.project.service;

import com.example.springbootassessment.project.domain.Project;
import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import com.example.springbootassessment.task.domain.Task;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();

    Project findById(Long id) throws ProjectNotFoundException;

    Project save(Project project);

    void deleteById(Long id) throws ProjectNotFoundException;

    Project update(Long id, Project project) throws ProjectNotFoundException;

    List<Task> findTasksByProjectId(Long projectId) throws ProjectNotFoundException;
}
