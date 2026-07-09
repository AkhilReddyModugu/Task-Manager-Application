package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.dto.TaskResponse;
import com.amodugu.taskmanager.entity.Task;
import com.amodugu.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(Task task) {
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<TaskResponse> getTaskById(Long id) {
        return taskRepository.findById(id).map(this::toResponse);
    }

    public List<TaskResponse> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId).stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(Task task) {
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getDueDate(),
                task.getProject().getId(), assigneeId
        );
    }
}