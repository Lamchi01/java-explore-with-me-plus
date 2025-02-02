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
import ewm.exception.InitiatorRequestException;
import ewm.exception.ValidationException;
import ewm.requests.repository.RequestRepository;
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
    private final RequestRepository requestRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(Long userId, Long eventId, InputCommentDto inputCommentDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, " Событие с ID - " + eventId + ", не найдено."));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Нельзя добавить комментарий к неопубликованному событию.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(Comment.class, " Нельзя оставлять комментарии к своему событию.");
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ValidationException(Comment.class, " Пользователь с ID - " + userId + ", не заявился на событие с ID - " + eventId + ".");
        }
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + userId + ", не найден."));
        Comment comment = commentMapper.toComment(inputCommentDto, author, event);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, InputCommentDto inputCommentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + userId + ", не найден."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, " Комментарии с ID - " + commentId + ", не найден."));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new InitiatorRequestException(" Нельзя редактировать комментарий другого пользователя.");
        }
        comment.setText(inputCommentDto.getText());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + userId + ", не найден."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, " Комментарии с ID - " + commentId + ", не найден."));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new InitiatorRequestException(" Нельзя удалить комментарий другого пользователя.");
        }
        commentRepository.deleteById(commentId);
    }
}
