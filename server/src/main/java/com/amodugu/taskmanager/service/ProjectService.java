package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.dto.CreateProjectRequest;
import com.amodugu.taskmanager.dto.ProjectResponse;
import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.entity.User;
import com.amodugu.taskmanager.exception.ForbiddenException;
import com.amodugu.taskmanager.exception.ResourceNotFoundException;
import com.amodugu.taskmanager.repository.ProjectRepository;
import com.amodugu.taskmanager.repository.TaskRepository;
import com.amodugu.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(CreateProjectRequest request, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = new Project();
        project.setOwner(owner);
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProjectResponse> getProjectById(Long id) {
        return projectRepository.findById(id).map(this::toResponse);
    }

    public List<ProjectResponse> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteProjectWithTasks(Long projectId, String requestingUsername) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Project not found"));

        if(!project.getOwner().getUsername().equals(requestingUsername)) {
            throw new ForbiddenException("You are not allowed to delete this project");
        }

        taskRepository.deleteByProjectId(projectId);
        projectRepository.deleteById(projectId);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getOwner().getId());
    }
}