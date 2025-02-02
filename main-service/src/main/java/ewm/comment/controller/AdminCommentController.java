package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(@PathVariable("eventId") long eventId, @Valid @RequestBody CommentDto commentDto) {

        return commentService.add(eventId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("commentId") long id) {
        commentService.delete(id);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable("commentId") long id,
                                 @Valid @RequestBody CommentDto commentDto) {
        return commentService.update(id, commentDto);
    }

}