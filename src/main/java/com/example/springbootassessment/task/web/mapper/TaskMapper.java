package com.example.springbootassessment.task.web.mapper;

import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.web.dto.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto map(Task task);

    com.example.springbootassessment.task.domain.Task toDomain(TaskDto taskDto);

    java.util.List<TaskDto> mapToDtoList(java.util.List<com.example.springbootassessment.task.domain.Task> tasks);

    java.util.List<com.example.springbootassessment.task.domain.Task> toDomainList(java.util.List<TaskDto> taskDtos);
}
