package com.example.taskmanagementsystem.commentary;

public interface CommentaryService {
    Commentary createComment(Long taskId, Commentary comment);

    void deleteComment(Long commentId, String currentUserEmail);

    Commentary updateComment(Long commentId, CommentaryDto commentDto, String currentUserEmail);
}
