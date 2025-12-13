package com.Application.SecureTaskManagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Application.SecureTaskManagement.dto.Response;
import com.Application.SecureTaskManagement.dto.TaskRequest;
import com.Application.SecureTaskManagement.entity.Task;
import com.Application.SecureTaskManagement.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Manual constructor injection instead of @RequiredArgsConstructor
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Response<Task>> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @PutMapping
    public ResponseEntity<Response<Task>> updateTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Task>>> getAllMyTasks() {
        return ResponseEntity.ok(taskService.getAllMyTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Task>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping("/status")
    public ResponseEntity<Response<List<Task>>> getMyTasksByCompletionStatus(
            @RequestParam boolean completed
    ) {
        return ResponseEntity.ok(taskService.getMyTasksByCompletionStatus(completed));
    }

    @GetMapping("/priority")
    public ResponseEntity<Response<List<Task>>> getMyTasksByPriority(
            @RequestParam String priority
    ) {
        return ResponseEntity.ok(taskService.getMyTasksByPriority(priority));
    }
}
