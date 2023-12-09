package com.example.taskmanagementsystem.commentary;

import jakarta.validation.constraints.Size;


public class CommentaryDto {
    @Size(max = 616, message = "Comment content must be less than 617 characters")
    private String content;
    private Long taskId;
    private String authorEmail;

    private String createdTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
