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
    public CommentDto add(Long userId, Long eventId, InputCommentDto inputCommentDto) {
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
    public CommentDto add(Long adminId, Long eventId, CommentDto commentDto) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + adminId + ", не найден."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, "Событие c ID - " + eventId + ", не найдено."));

        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto, admin, event)));
    }

    @Override
    public void delete(Long userId, Long commentId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + userId + ", не найден."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, " Комментарии с ID - " + commentId + ", не найден."));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new InitiatorRequestException(" Нельзя удалить комментарий другого пользователя.");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto update(Long userId, Long commentId, InputCommentDto inputCommentDto) {
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
    public CommentDto update(Long id, CommentDto commentDto) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Comment.class, "Комментарий c ID - " + id + ", не найден."));

        if (commentDto.getAuthor() != null) {
            User author = userRepository.findById(commentDto.getAuthor().getId())
                    .orElseThrow(() -> new EntityNotFoundException(User.class, " Пользователь с ID - " + commentDto.getAuthor().getId() + ", не найден."));
            comment.setAuthor(author);
        }

        if (commentDto.getEventId() != null) {
            Event event = eventRepository.findById(commentDto.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException(Event.class, "Событие c ID - " + id + ", не найдено."));
            comment.setEvent(event);
        }

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }

        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        }
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

}
