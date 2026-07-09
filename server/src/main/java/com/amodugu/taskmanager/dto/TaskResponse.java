package com.amodugu.taskmanager.dto;

import com.amodugu.taskmanager.entity.TaskStatus;
import com.amodugu.taskmanager.entity.TaskPriority;

import java.time.LocalDate;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Long projectId;
    private Long assigneeId;

    public TaskResponse(Long id, String title, String description, TaskStatus status,
                        TaskPriority priority, LocalDate dueDate, Long projectId, Long assigneeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public Long getProjectId() { return projectId; }
    public Long getAssigneeId() { return assigneeId; }
}