package com.example.springbootassessment.task.web.controller;

import com.example.springbootassessment.factories.TaskEntityFactory;
import com.example.springbootassessment.project.repository.ProjectRepository;
import com.example.springbootassessment.task.domain.Task;
import com.example.springbootassessment.task.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TaskIntegrationTest {

    @Autowired
    // resources on how to use in https://docs.spring.io/spring-framework/reference/testing/webtestclient.html#webtestclient-no-content
    private WebTestClient webTestClient;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("should load all tasks")
    void shouldLoadAllTasks() {
        // given
        IntStream.range(0, 10)
                .mapToObj(i -> TaskEntityFactory.create())
                .forEach(taskRepository::save);

        // when
        var responseBody = webTestClient
                .get()
                .uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(responseBody).hasSize(10);
    }

    @Test
    @DisplayName("should load task by project id")
    @Sql(value = "/data/setup-task-with-project.sql", executionPhase = BEFORE_TEST_METHOD)
    void shouldLoadTasksByProjectId() {
        // given
        var projectId = 1L;

        // when
        var responseBody = webTestClient
                .get()
                .uri("/tasks/" + projectId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Task.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(responseBody).hasSize(5);
    }

    @Test
    @DisplayName("given project id does not exist, when loading tasks by project id, then should return 404")
    void givenProjectIdDoesNotExistWhenLoadingTasksByProjectIdThenShouldReturn404() {
        // given
        var nonExistentProjectId = 999L;

        // when then
        webTestClient
                .get()
                .uri("/tasks/" + nonExistentProjectId)
                .exchange()
                .expectStatus().isNotFound();
    }
}