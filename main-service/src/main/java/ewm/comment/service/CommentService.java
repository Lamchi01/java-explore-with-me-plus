package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, InputCommentDto inputCommentDto);

    CommentDto updateComment(Long userId, Long commentId, InputCommentDto inputCommentDto);

    void deleteComment(Long userId, Long commentId);
}