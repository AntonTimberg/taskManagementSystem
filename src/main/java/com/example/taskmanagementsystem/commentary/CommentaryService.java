package com.example.taskmanagementsystem.commentary;

public interface CommentaryService {
    public Commentary createComment(Long taskId, Commentary comment);

    public void deleteComment(Long commentId);

    public Commentary updateComment(Long commentId, String newContent);
}
