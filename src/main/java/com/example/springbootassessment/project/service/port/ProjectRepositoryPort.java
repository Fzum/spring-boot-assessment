package com.example.springbootassessment.project.service.port;

import com.example.springbootassessment.project.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {
    Optional<Project> findById(Long id);
    List<Project> findAll();
    Project save(Project project);
    void deleteById(Long id);
    boolean existsById(Long id);
}
