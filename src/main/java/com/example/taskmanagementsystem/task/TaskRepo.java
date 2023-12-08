package com.example.taskmanagementsystem.task;

import com.example.taskmanagementsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findByAuthorIdOrAssigneeId(Long authorId, Long assigneeId, Pageable pageable);

    List<Task> findByAssignee(User assignee);

    List<Task> findByAuthor(User author);

    List<Task> findByStatus(TaskStatus status);

    Page<Task> findByAssignee(User assignee, Pageable pageable);

    Page<Task> findByAuthor(User author, Pageable pageable);
}
