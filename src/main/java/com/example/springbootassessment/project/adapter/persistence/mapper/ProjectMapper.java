package com.example.springbootassessment.project.adapter.persistence.mapper;

import com.example.springbootassessment.project.domain.Project;
import com.example.springbootassessment.project.persistence.ProjectEntity;
import com.example.springbootassessment.task.adapter.persistence.mapper.TaskMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ProjectMapper {
    Project toDomain(ProjectEntity entity);
    ProjectEntity toEntity(Project domain);
    List<Project> toDomainList(List<ProjectEntity> entities);
}
