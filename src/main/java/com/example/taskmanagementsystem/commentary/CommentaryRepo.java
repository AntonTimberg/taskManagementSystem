package com.example.taskmanagementsystem.commentary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentaryRepo extends JpaRepository<Commentary, Long> {
    Page<Commentary> findByTaskId(Long taskId, Pageable pageable);
}
