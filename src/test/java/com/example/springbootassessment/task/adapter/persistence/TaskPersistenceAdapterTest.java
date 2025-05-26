package com.example.springbootassessment.task.adapter.persistence;

import com.example.springbootassessment.task.adapter.persistence.mapper.TaskMapper;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.persistence.TaskEntity;
import com.example.springbootassessment.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Rollback transactions after each test
class TaskPersistenceAdapterTest {

    @Autowired
    private TaskPersistenceAdapter taskPersistenceAdapter;

    @Autowired
    private TaskRepository taskRepository; // JPA repository for verification

    @Autowired
    private TaskMapper taskPersistenceMapper; // The persistence mapper

    @Test
    void save_shouldPersistTaskAndReturnDomainObject() {
        // 1. Create a Task domain object
        LocalDateTime dueDate = LocalDateTime.now().plusDays(5).withNano(0); // Remove nanos for DB comparison
        Task taskDomainToSave = new Task(null, // ID is null for new task
                "New Test Task",
                "Description for new test task",
                false,
                dueDate);

        // 2. Call taskPersistenceAdapter.save(taskDomain)
        Task savedTaskDomain = taskPersistenceAdapter.save(taskDomainToSave);

        // 3. Assert that the returned domain object has an ID
        assertNotNull(savedTaskDomain.id(), "Saved task domain object should have an ID.");
        assertEquals(taskDomainToSave.title(), savedTaskDomain.title());
        assertEquals(taskDomainToSave.description(), savedTaskDomain.description());
        assertEquals(taskDomainToSave.completed(), savedTaskDomain.completed());
        assertEquals(taskDomainToSave.dueDate(), savedTaskDomain.dueDate());


        // 4. Use taskRepository.findById() (JPA) to fetch the TaskEntity
        Optional<TaskEntity> optionalEntity = taskRepository.findById(savedTaskDomain.id());
        assertTrue(optionalEntity.isPresent(), "TaskEntity should be found in the database.");
        TaskEntity fetchedEntity = optionalEntity.get();

        // 5. Assert that the entity was saved correctly and its fields match the domain object (after mapping)
        assertEquals(savedTaskDomain.id(), fetchedEntity.getId());
        assertEquals(taskDomainToSave.title(), fetchedEntity.getTitle());
        assertEquals(taskDomainToSave.description(), fetchedEntity.getDescription());
        assertEquals(taskDomainToSave.completed(), fetchedEntity.isCompleted());
        assertEquals(taskDomainToSave.dueDate(), fetchedEntity.getDueDate());

        // Optional: Verify mapping back from fetched entity to domain
        Task mappedDomainFromFetchedEntity = taskPersistenceMapper.toDomain(fetchedEntity);
        assertEquals(savedTaskDomain, mappedDomainFromFetchedEntity);
    }

    // Additional tests for other methods in TaskPersistenceAdapter can be added here.
    // For example: findById, findAll, deleteById, findAllByProjectId, existsById
}
