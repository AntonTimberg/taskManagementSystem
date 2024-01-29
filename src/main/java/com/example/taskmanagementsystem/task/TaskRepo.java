package com.example.taskmanagementsystem.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findByAuthorEmail(String authorEmail, Pageable pageable);
    Page<Task> findByAssigneeEmail(String assigneeEmail, Pageable pageable);
    Page<Task> findByTitleContaining(String title, Pageable pageable);
}
