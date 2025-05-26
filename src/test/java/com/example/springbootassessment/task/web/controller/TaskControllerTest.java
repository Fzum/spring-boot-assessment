package com.example.springbootassessment.task.web.controller;

import com.example.springbootassessment.common.web.advice.GlobalExceptionHandler;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.domain.exception.TaskNotFoundException;
import com.example.springbootassessment.task.service.TaskService;
import com.example.springbootassessment.task.web.dto.TaskDto;
import com.example.springbootassessment.task.web.mapper.TaskMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(GlobalExceptionHandler.class) // Import the advice
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskWebMapper; // Name as in TaskController

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTaskDomain;
    private TaskDto sampleTaskDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        sampleTaskDomain = new Task(1L, "Test Task", "Description", false, now);
        sampleTaskDto = new TaskDto(1L, "Test Task DTO", "Description DTO", false, now);
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        List<Task> tasks = Collections.singletonList(sampleTaskDomain);
        List<TaskDto> taskDtos = Collections.singletonList(sampleTaskDto);

        when(taskService.findAll()).thenReturn(tasks);
        when(taskWebMapper.mapToDtoList(tasks)).thenReturn(taskDtos);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(sampleTaskDto.id()))
                .andExpect(jsonPath("$[0].title").value(sampleTaskDto.title()));
    }

    @Test
    void getTaskById_whenTaskFound_shouldReturnTaskDto() throws Exception {
        when(taskService.findById(1L)).thenReturn(sampleTaskDomain);
        when(taskWebMapper.map(sampleTaskDomain)).thenReturn(sampleTaskDto);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleTaskDto.id()))
                .andExpect(jsonPath("$.title").value(sampleTaskDto.title()));
    }

    @Test
    void getTaskById_whenTaskNotFound_shouldReturn404() throws Exception {
        when(taskService.findById(1L)).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_shouldReturnCreatedTaskDto() throws Exception {
        TaskDto inputDto = new TaskDto(null, "New Task", "New Desc", false, LocalDateTime.now());
        Task createdDomainTask = new Task(2L, "New Task", "New Desc", false, inputDto.dueDate());
        TaskDto createdDto = new TaskDto(2L, "New Task", "New Desc", false, inputDto.dueDate());

        when(taskWebMapper.toDomain(any(TaskDto.class))).thenReturn(createdDomainTask); // Map DTO to Domain
        when(taskService.save(any(Task.class))).thenReturn(createdDomainTask); // Service saves Domain
        when(taskWebMapper.map(any(Task.class))).thenReturn(createdDto); // Map Domain back to DTO

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdDto.id()))
                .andExpect(jsonPath("$.title").value(createdDto.title()));
    }
    
    @Test
    void updateTask_whenTaskExists_shouldReturnUpdatedTaskDto() throws Exception {
        Long taskId = 1L;
        TaskDto inputDto = new TaskDto(taskId, "Updated Task", "Updated Desc", true, LocalDateTime.now());
        Task updatedDomainTask = new Task(taskId, inputDto.title(), inputDto.description(), inputDto.completed(), inputDto.dueDate());
        TaskDto outputDto = new TaskDto(taskId, inputDto.title(), inputDto.description(), inputDto.completed(), inputDto.dueDate());

        when(taskWebMapper.toDomain(any(TaskDto.class))).thenReturn(updatedDomainTask);
        when(taskService.update(eq(taskId), any(Task.class))).thenReturn(updatedDomainTask);
        when(taskWebMapper.map(any(Task.class))).thenReturn(outputDto);

        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(outputDto.id()))
                .andExpect(jsonPath("$.title").value(outputDto.title()))
                .andExpect(jsonPath("$.completed").value(outputDto.completed()));
    }

    @Test
    void updateTask_whenTaskNotFound_shouldReturn404() throws Exception {
        Long taskId = 1L;
        TaskDto inputDto = new TaskDto(taskId, "Updated Task", "Updated Desc", true, LocalDateTime.now());

        when(taskWebMapper.toDomain(any(TaskDto.class))).thenReturn(new Task(taskId, inputDto.title(), inputDto.description(), inputDto.completed(), inputDto.dueDate()));
        when(taskService.update(eq(taskId), any(Task.class))).thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_whenTaskExists_shouldReturnNoContent() throws Exception {
        Long taskId = 1L;
        // taskService.deleteById is void, so no need to mock return value if successful
        // Mockito.doNothing().when(taskService).deleteById(taskId); // This is default behavior for void methods

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_whenTaskNotFound_shouldReturn404() throws Exception {
        Long taskId = 1L;
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteById(taskId);

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).deleteById(taskId);
    }

    @Test
    void getTasksByProjectId_shouldReturnListOfTasks() throws Exception {
        Long projectId = 1L;
        List<Task> tasks = Collections.singletonList(sampleTaskDomain);
        List<TaskDto> taskDtos = Collections.singletonList(sampleTaskDto);

        when(taskService.findAllByProjectId(projectId)).thenReturn(tasks);
        when(taskWebMapper.mapToDtoList(tasks)).thenReturn(taskDtos);

        mockMvc.perform(get("/tasks/project/" + projectId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(sampleTaskDto.id()))
                .andExpect(jsonPath("$[0].title").value(sampleTaskDto.title()));
    }
}
