package com.amodugu.taskmanager.controller;

import com.amodugu.taskmanager.dto.CreateProjectRequest;
import com.amodugu.taskmanager.dto.ProjectResponse;
import com.amodugu.taskmanager.dto.UpdateProjectRequest;
import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest project, @AuthenticationPrincipal UserDetails user) {
        ProjectResponse createdProject = projectService.createProject(project, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(@AuthenticationPrincipal UserDetails currentUser) {
        System.out.println("Request made by: " + currentUser.getUsername());
        List<ProjectResponse> allProjects = projectService.getAllProjects();
        return ResponseEntity.ok(allProjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public List<ProjectResponse> getProjectsByOwner(@PathVariable Long ownerId) {
        return projectService.getProjectsByOwner(ownerId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody UpdateProjectRequest request, @AuthenticationPrincipal UserDetails currentUser) {
        ProjectResponse response= projectService.updateProject(id,request,currentUser.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        projectService.deleteProjectWithTasks(id, currentUser.getUsername());
        return ResponseEntity.noContent().build();
    }
}
