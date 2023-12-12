package com.example.taskmanagementsystem;

import com.example.taskmanagementsystem.commentary.CommentaryRepo;
import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskRepo;
import com.example.taskmanagementsystem.task.TaskServiceImpl;
import com.example.taskmanagementsystem.user.User;
import com.example.taskmanagementsystem.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

public class TaskServiceImplTest {
    @Mock
    private TaskRepo taskRepository;

    @Mock
    private CommentaryRepo commentaryRepo;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(User author) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(author.getEmail(), author.getPassword(), authorities);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, author.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void mockSecurityContext() {
        User defaultUser = new User();
        defaultUser.setName("Default User");
        defaultUser.setEmail("default@example.com");
        defaultUser.setPassword("password");
        defaultUser.setRole(UserRole.CLIENT);

        mockSecurityContext(defaultUser);
    }

    @Test
    void givenValidTaskWhenCreate() {
        mockSecurityContext();
        Task mockTask = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.createTask(mockTask);

        assertNotNull(result);
        verify(taskRepository).save(mockTask);
    }

    @Test
    void givenValidTaskWhenUpdate() {
        Long taskId = 1L;
        Task existingTask = new Task();

        User author = new User();
        author.setName("Test Name");
        author.setEmail("test@example.com");
        author.setPassword("password");
        author.setRole(UserRole.CLIENT);

        mockSecurityContext(author);
        existingTask.setAuthor(author);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(taskId, updatedTask);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository).save(any(Task.class));

    }
}
