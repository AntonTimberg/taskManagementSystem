package com.example.taskmanagementsystem.commentary;

import com.example.taskmanagementsystem.task.Task;
import com.example.taskmanagementsystem.task.TaskService;
import com.example.taskmanagementsystem.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommentaryConverter {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    public Commentary convert(CommentaryDto commentDto) {
        Commentary comment = new Commentary();
        comment.setContent(commentDto.getContent());

        Task task = taskService.getTaskById(commentDto.getTaskId());
        comment.setTask(task);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            userService.getUserByEmail(email).ifPresent(comment::setAuthor);
        }

        return comment;
    }

    public CommentaryDto convert(Commentary comment) {
        CommentaryDto commentDto = new CommentaryDto();
        commentDto.setContent(comment.getContent());
        commentDto.setTaskId(comment.getTask().getId());
        if (comment.getAuthor() != null) {
            commentDto.setAuthorEmail(comment.getAuthor().getEmail());
        } else {
            commentDto.setAuthorEmail("Anonymous");
        }

        if (comment.getCreatedTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd mm yyyy HH:mm");
            String formattedDateTime = comment.getCreatedTime().format(formatter);
            commentDto.setCreatedTime(formattedDateTime);
        }

        return commentDto;
    }

}
