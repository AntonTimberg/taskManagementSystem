package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User assignee);

    List<Task> findByAuthor(User author);

    Page<Task> findByAssignee(User assignee, Pageable pageable);

    Page<Task> findByAuthor(User author, Pageable pageable);

    List<Task> findByAuthorEmail(String authorEmail);
    List<Task> findByAssigneeEmail(String assigneeEmail);
    Page<Task> findByTitleContaining(String title, Pageable pageable);

}
