package com.amodugu.taskmanager.service;

import com.amodugu.taskmanager.entity.Project;
import com.amodugu.taskmanager.entity.User;
import com.amodugu.taskmanager.exception.ForbiddenException;
import com.amodugu.taskmanager.repository.ProjectRepository;
import com.amodugu.taskmanager.repository.TaskRepository;
import com.amodugu.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void deleteProject_whenNotOwner_throwsForbidden() {
        // Arrange: build fake data by hand, no database involved
        User actualOwner = new User();
        actualOwner.setUsername("manju");

        Project project = new Project();
        project.setId(1L);
        project.setOwner(actualOwner);

        // Tell the mock: "when findById(1L) is called, return this project"
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act + Assert: calling as a DIFFERENT user should throw ForbiddenException
        assertThatThrownBy(() -> projectService.deleteProjectWithTasks(1L, "someone_else"))
                .isInstanceOf(ForbiddenException.class);
    }
}