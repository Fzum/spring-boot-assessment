package com.example.springbootassessment.project.adapter.persistence;

import com.example.springbootassessment.project.adapter.persistence.mapper.ProjectMapper;
import com.example.springbootassessment.project.domain.Project;
import com.example.springbootassessment.project.repository.ProjectRepository;
import com.example.springbootassessment.project.service.port.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id).map(projectMapper::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return projectMapper.toDomainList(projectRepository.findAll());
    }

    @Override
    public Project save(Project project) {
        var projectEntity = projectMapper.toEntity(project);
        var savedEntity = projectRepository.save(projectEntity);
        return projectMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }
}
