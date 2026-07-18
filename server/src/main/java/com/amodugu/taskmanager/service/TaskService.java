package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.dto.CreateTaskRequest;
import com.amodugu.taskmanager.dto.TaskResponse;
import com.amodugu.taskmanager.dto.UpdateTaskRequest;
import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.entity.Task;
import com.amodugu.taskmanager.entity.User;
import com.amodugu.taskmanager.exception.ForbiddenException;
import com.amodugu.taskmanager.exception.ResourceNotFoundException;
import com.amodugu.taskmanager.repository.ProjectRepository;
import com.amodugu.taskmanager.repository.TaskRepository;
import com.amodugu.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        task.setAssignee(assignee);

        return toResponse(taskRepository.save(task));
    }

    // getALlTasks() with pagination
    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(this::toResponse);
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

    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if(request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if(request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if(request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if(request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if(request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if(request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }
        return  toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long taskId, String requestingUsername) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getOwner().getUsername().equals(requestingUsername)) {
            throw new ForbiddenException("You are not allowed to delete this task");
        }

        taskRepository.deleteById(taskId);
    }

    private TaskResponse toResponse(Task task) {
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getDueDate(),
                task.getProject().getId(), assigneeId
        );
    }

    public List<String> getTaskSummariesForAssignee(Long assigneeId) {
        List<Task> tasks = taskRepository.findByAssigneeIdWithProject(assigneeId);
        return tasks.stream()
                .map(t -> t.getTitle() + " — " + t.getProject().getName())
                .toList();
    }

}