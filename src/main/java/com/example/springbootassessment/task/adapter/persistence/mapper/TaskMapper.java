package com.example.springbootassessment.task.adapter.persistence.mapper;

import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.persistence.TaskEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toDomain(TaskEntity entity);
    TaskEntity toEntity(Task domain);
    List<Task> toDomainList(List<TaskEntity> entities);
}
