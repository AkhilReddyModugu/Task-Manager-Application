package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.dto.CreateProjectRequest;
import com.amodugu.taskmanager.dto.ProjectResponse;
import com.amodugu.taskmanager.dto.UpdateProjectRequest;
import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.entity.User;
import com.amodugu.taskmanager.exception.ForbiddenException;
import com.amodugu.taskmanager.exception.ResourceNotFoundException;
import com.amodugu.taskmanager.repository.ProjectRepository;
import com.amodugu.taskmanager.repository.TaskRepository;
import com.amodugu.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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

        Project saved = projectRepository.save(project);
        log.info("Created project {} for owner {}", saved.getId(), ownerUsername);
        return toResponse(saved);
    }

    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::toResponse);
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
            log.warn("User {} attempted to delete project {} without permission", requestingUsername, projectId);
            throw new ForbiddenException("You are not allowed to delete this project");
        }

        taskRepository.deleteByProjectId(projectId);
        projectRepository.deleteById(projectId);
        log.info("Deleted project {} and its tasks", projectId);
    }

    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest request, String requestingUsername) {
        Project project= projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Project not found"));

        if(!project.getOwner().getUsername().equals(requestingUsername)){
            log.warn("User {} attempted to update project {} without permission", requestingUsername, projectId);
            throw new ForbiddenException("You are not allowed to update this project");
        }
        if(request.getName() != null) {
            project.setName(request.getName());
        }
        if(request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        log.info("Updated project {}", projectId);
        return toResponse(projectRepository.save(project));
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getOwner().getId(),
                project.getCreatedAt(), project.getUpdatedAt());
    }
}