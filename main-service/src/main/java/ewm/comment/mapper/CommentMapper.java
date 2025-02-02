package ewm.comment.mapper;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.InputCommentDto;
import ewm.comment.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toComment(InputCommentDto inputCommentDto);

    @Mapping(target = "eventId", source = "event.id")
    CommentDto toCommentDto(Comment comment);
}