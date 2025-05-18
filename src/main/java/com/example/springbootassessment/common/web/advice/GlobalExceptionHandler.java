package com.example.springbootassessment.common.web.advice;

import com.example.springbootassessment.project.domain.exception.ProjectNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTaskNotFound(ProjectNotFoundException ex) {
        var problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(problemDetail);
    }
}
