package com.example.taskmanagementsystem.task;

import io.swagger.v3.oas.annotations.media.Schema;

public class TaskDto {
    @Schema(description = "идентификатор задачи")
    private Long id;
    @Schema(description = "название задачи")
    private String title;
    @Schema(description = "описание задачи")
    private String description;
    @Schema(description = "статус задачи")
    private String status;
    @Schema(description = "приоритет задачи")
    private String priority;
    @Schema(description = "e-mail исполнителя")
    private String assigneeEmail;
    @Schema(description = "e-mail автора")
    private String authorEmail;
    @Schema(description = "время создания задачи")
    private String createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
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

