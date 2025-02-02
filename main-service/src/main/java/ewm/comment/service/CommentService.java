package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, InputCommentDto inputCommentDto);

    CommentDto add(Long adminId, Long eventId, CommentDto commentDto);

    CommentDto updateComment(Long userId, Long commentId, InputCommentDto inputCommentDto);

    CommentDto update(Long id, CommentDto commentDto);

    void delete(Long id);

    void deleteComment(Long userId, Long commentId);

}