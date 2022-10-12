package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentEntity toComment(CommentDto commentDto) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentDto.getId());
        commentEntity.setText(commentDto.getText());
        commentEntity.setCreated(LocalDateTime.now());
        return commentEntity;
    }

    public static CommentDto toCommentDto(CommentEntity comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static List<CommentDto> toCommentDto(List<CommentEntity> comments) {
        List<CommentDto> result = new ArrayList<>();
        for (CommentEntity comment : comments) {
            result.add(toCommentDto(comment));
        }
        return result;
    }
}
