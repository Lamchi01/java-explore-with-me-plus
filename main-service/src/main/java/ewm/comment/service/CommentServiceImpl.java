package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;
import ewm.comment.mapper.CommentMapper;
import ewm.comment.model.Comment;
import ewm.comment.repository.CommentRepository;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.repository.EventRepository;
import ewm.exception.ConditionNotMetException;
import ewm.exception.EntityNotFoundException;
import ewm.user.model.User;
import ewm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(Long userId, Long eventId, InputCommentDto inputCommentDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, " Событие с ID - " + eventId + ", не найдено."));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Нельзя добавить комментарий к неопубликованному событию.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + userId + ", не найден."));
        Comment comment = commentMapper.toComment(inputCommentDto);
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, InputCommentDto inputCommentDto) {
        return null;
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {

    }
}
