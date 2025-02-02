package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, InputCommentDto inputCommentDto);

    CommentDto updateComment(Long userId, Long commentId, InputCommentDto inputCommentDto);

    CommentDto add(Long eventId, CommentDto commentDto);

    void delete(Long id);

    CommentDto update(Long id, CommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

}