package com.example.taskmanagementsystem.commentary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;


public class CommentaryDto {
    @Size(max = 616, message = "Comment content must be less than 617 characters")
    @Schema(description = "Текст комментария", required = true)
    private String content;
    @Schema(description = "Email пользователя, оставившего комментарий")
    private String authorEmail;
    @Schema(description = "Время создания комментария")
    private String createdTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
