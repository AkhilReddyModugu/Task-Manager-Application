package com.amodugu.taskmanager.controller;

import com.amodugu.taskmanager.dto.CreateTaskRequest;
import com.amodugu.taskmanager.dto.TaskResponse;
import com.amodugu.taskmanager.dto.UpdateTaskRequest;
import com.amodugu.taskmanager.entity.Task;
import com.amodugu.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request, @AuthenticationPrincipal UserDetails currentUser) {
        TaskResponse saved = taskService.createTask(request,currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(Pageable pageable) {
        Page<TaskResponse> response= taskService.getAllTasks(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/assignee/{assigneeId}")
    public List<TaskResponse> getTasksByAssignee(@PathVariable Long assigneeId) {
        return taskService.getTasksByAssignee(assigneeId);
    }

    @GetMapping("/assignee/{assigneeId}/summary")
    public List<String> getTaskSummaries(@PathVariable Long assigneeId) {
        return taskService.getTaskSummariesForAssignee(assigneeId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request, @AuthenticationPrincipal UserDetails currentUser) {
        TaskResponse updated= taskService.updateTask(id,request,currentUser.getUsername());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        taskService.deleteTask(id,currentUser.getUsername());
        return ResponseEntity.noContent().build();
    }

}