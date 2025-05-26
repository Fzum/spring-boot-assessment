package com.example.springbootassessment.project.web.mapper;

import com.example.springbootassessment.project.domain.Project;
import com.example.springbootassessment.project.web.dto.ProjectDto;
import com.example.springbootassessment.task.web.mapper.TaskMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ProjectMapper {
    ProjectDto toDto(Project project);
    Project toDomain(ProjectDto projectDto);
    List<ProjectDto> toDtoList(List<Project> projects);
}
