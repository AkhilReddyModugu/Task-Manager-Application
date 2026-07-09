package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.dto.ProjectResponse;
import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse createProject(Project project) {
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

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getOwner().getId());
    }
}