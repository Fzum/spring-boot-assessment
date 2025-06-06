package com.example.springbootassessment.task.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TaskIntegrationTest {

    @Autowired
    // resources on how to use in https://docs.spring.io/spring-framework/reference/testing/webtestclient.html#webtestclient-no-content
    private WebTestClient webTestClient;

    @AfterEach
    void tearDown() {
        // todo: make sure that after the test all data is clean
    }

    @Test
    @DisplayName("should load all tasks")
    void shouldLoadAllTasks() {
        // given

        // when

        // then
        fail("todo: implement");
    }

    @Test
    @DisplayName("should load task by project id")
    @Sql(value = "/data/setup-task-with-project.sql", executionPhase = BEFORE_TEST_METHOD)
    void shouldLoadTasksByProjectId() {
        // given

        // when

        // then
        fail("todo: implement");
    }

    @Test
    @DisplayName("given project id does not exist, when loading tasks by project id, then should return 404")
    void givenProjectIdDoesNotExistWhenLoadingTasksByProjectIdThenShouldReturn404() {
        // given

        // when

        // then
        fail("todo: implement");
    }
}