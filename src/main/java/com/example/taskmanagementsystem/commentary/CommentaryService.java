package com.example.taskmanagementsystem.commentary;

public interface CommentaryService {
    Commentary createComment(Long taskId, Commentary comment);

    void deleteComment(Long commentId);

    Commentary updateComment(Long commentId, CommentaryDto commentDto);
}
