package ewm.comment.dto;

import jakarta.validation.constraints.NotBlank;

//DTO Входящее дто с текстом
public class InputCommentDto {
    @NotBlank
    private String text;
}