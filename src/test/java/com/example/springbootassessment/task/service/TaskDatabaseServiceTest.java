package com.example.springbootassessment.task.service;

import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import com.example.springbootassessment.project.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskDatabaseServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskDatabaseService taskDatabaseService;

    @Test
    @DisplayName("given project is not present, then should throw ProjectNotFoundException")
    void givenProjectIsNotPresentThenShouldThrowProjectNotFoundException() {
        // given
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> taskDatabaseService.findAllByProjectId(1L))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("project with id 1 not found");
    }

}