package com.example.taskmanagementsystem;

import com.example.taskmanagementsystem.commentary.Commentary;
import com.example.taskmanagementsystem.commentary.CommentaryDto;
import com.example.taskmanagementsystem.commentary.CommentaryRepo;
import com.example.taskmanagementsystem.commentary.CommentaryServiceImpl;
import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskRepo;
import com.example.taskmanagementsystem.task.TaskServiceImpl;
import com.example.taskmanagementsystem.task.TaskStatus;
import com.example.taskmanagementsystem.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepo taskRepository;

    @Mock
    private CommentaryRepo commentaryRepo;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @InjectMocks
    private TaskServiceImpl taskService;

    @InjectMocks
    private CommentaryServiceImpl commentaryService;

    @Test
    public void testCreateTask(){
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task created = taskService.createTask(task);

        verify(taskRepository).save(task);
        assertNotNull(created);
    }

    @Test
    public void testGetTaskById(){
        Long taskId = 1L;
        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task found = taskService.getTaskById(taskId);

        verify(taskRepository).findById(taskId);
        assertNotNull(found);
    }

    @Test
    public void testUpdateTask(){
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setTitle("Old Title");

        User author = new User();
        author.setEmail("author@example.com");
        existingTask.setAuthor(author);

        Task updatedInfo = new Task();
        updatedInfo.setTitle("New Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedInfo);

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("author@example.com");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        Task result = taskService.updateTask(taskId, updatedInfo);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(taskRepository).save(existingTask);
    }

    @Test
    public void testDeleteTask() {
        Long taskId = 1L;
        Task taskToDelete = new Task();
        taskToDelete.setId(taskId);

        User author = new User();
        author.setEmail("author@example.com");
        taskToDelete.setAuthor(author);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskToDelete));

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("author@example.com");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        taskService.deleteTask(taskId);

        verify(taskRepository).delete(taskToDelete);
    }

    @Test
    public void testGetTaskByAuthor(){
        String authorEmail = "author@example.com";
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> expectedPage = new PageImpl<>(Collections.singletonList(new Task()));

        when(taskRepository.findByAuthorEmail(authorEmail, pageable)).thenReturn(expectedPage);

        Page<Task> result = taskService.getTasksByAuthor(authorEmail, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(taskRepository).findByAuthorEmail(authorEmail, pageable);
    }

    @Test
    public void testUpdateTaskStatus() {
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskStatus(taskId, newStatus);

        assertNotNull(updatedTask);
        assertEquals(newStatus, updatedTask.getStatus());
        verify(taskRepository).save(task);
        verify(taskRepository).findById(taskId);
    }

    @Test
    public void testAssignTask() {
        Long taskId = 1L;
        User newAssignee = new User();
        newAssignee.setEmail("assignee@example.com");
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task assignedTask = taskService.assignTask(taskId, newAssignee);

        assertNotNull(assignedTask);
        assertEquals(newAssignee, assignedTask.getAssignee());
        verify(taskRepository).save(task);
        verify(taskRepository).findById(taskId);
    }

    @Test
    public void testFindTasks_WithFilter() {
        String filter = "testFilter";
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> expectedPage = new PageImpl<>(Collections.singletonList(new Task()));

        when(taskRepository.findByTitleContaining(filter, pageable)).thenReturn(expectedPage);

        Page<Task> result = taskService.findTasks(filter, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    public void testFindTasks_NoFilter() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> expectedPage = new PageImpl<>(Collections.singletonList(new Task()));

        when(taskRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Task> result = taskService.findTasks("", pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    public void testGetCommentsForTask() {
        Long taskId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Commentary> expectedPage = new PageImpl<>(Collections.singletonList(new Commentary()));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));
        when(commentaryRepo.findByTaskId(taskId, pageable)).thenReturn(expectedPage);

        Page<Commentary> result = taskService.getCommentsForTask(taskId, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
    }

    @Test
    public void testGetTasksByAssignee() {
        String assigneeEmail = "assignee@example.com";
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> expectedPage = new PageImpl<>(Collections.singletonList(new Task()));

        when(taskRepository.findByAssigneeEmail(assigneeEmail, pageable)).thenReturn(expectedPage);

        Page<Task> result = taskService.getTasksByAssignee(assigneeEmail, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(taskRepository).findByAssigneeEmail(assigneeEmail, pageable);
    }

    @Test
    public void testCreateComment_TaskExists() {
        Long taskId = 1L;
        Commentary comment = new Commentary();
        comment.setContent("Test comment");
        Task task = new Task();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentaryRepo.save(any(Commentary.class))).thenReturn(comment);

        Commentary createdComment = commentaryService.createComment(taskId, comment);

        assertNotNull(createdComment);
        assertEquals("Test comment", createdComment.getContent());
        verify(commentaryRepo).save(comment);
    }

    @Test
    public void testCreateComment_TaskDoesNotExist() {
        Long taskId = 1L;
        Commentary comment = new Commentary();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentaryService.createComment(taskId, comment);
        });
    }

    @Test
    public void testDeleteComment_AuthorizedUser() {
        Long commentId = 1L;
        String currentUserEmail = "author@example.com";
        Commentary comment = new Commentary();
        User author = new User();
        author.setEmail(currentUserEmail);
        comment.setAuthor(author);

        when(commentaryRepo.findById(commentId)).thenReturn(Optional.of(comment));

        commentaryService.deleteComment(commentId, currentUserEmail);

        verify(commentaryRepo).delete(comment);
    }

    @Test
    public void testDeleteComment_UnauthorizedUser() {
        Long commentId = 1L;
        String currentUserEmail = "unauthorized@example.com";
        Commentary comment = new Commentary();
        User author = new User();
        author.setEmail("author@example.com");
        comment.setAuthor(author);

        when(commentaryRepo.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(RuntimeException.class, () -> {
            commentaryService.deleteComment(commentId, currentUserEmail);
        });
    }

    @Test
    public void testUpdateComment_AuthorizedUser() {
        Long commentId = 1L;
        String currentUserEmail = "author@example.com";
        Commentary existingComment = new Commentary();
        User author = new User();
        author.setEmail(currentUserEmail);
        existingComment.setAuthor(author);
        CommentaryDto commentDto = new CommentaryDto();
        commentDto.setContent("Updated content");

        when(commentaryRepo.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentaryRepo.save(any(Commentary.class))).thenReturn(existingComment);

        Commentary updatedComment = commentaryService.updateComment(commentId, commentDto, currentUserEmail);

        assertEquals("Updated content", updatedComment.getContent());
        verify(commentaryRepo).save(existingComment);
    }

    @Test
    public void testUpdateComment_UnauthorizedUser() {
        Long commentId = 1L;
        String currentUserEmail = "unauthorized@example.com";
        Commentary existingComment = new Commentary();
        User author = new User();
        author.setEmail("author@example.com");
        existingComment.setAuthor(author);
        CommentaryDto commentDto = new CommentaryDto();

        when(commentaryRepo.findById(commentId)).thenReturn(Optional.of(existingComment));

        assertThrows(RuntimeException.class, () -> {
            commentaryService.updateComment(commentId, commentDto, currentUserEmail);
        });
    }
}
