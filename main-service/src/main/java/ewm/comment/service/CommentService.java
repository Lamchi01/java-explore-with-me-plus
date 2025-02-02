package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;

public interface CommentService {

    CommentDto add(Long userId, Long eventId, InputCommentDto inputCommentDto);

    CommentDto add(Long adminId, Long eventId, CommentDto commentDto);

    CommentDto update(Long userId, Long commentId, InputCommentDto inputCommentDto);

    CommentDto update(Long id, CommentDto commentDto);

    void delete(Long id);

    void delete(Long userId, Long commentId);

}