package ru.practicum.shareit.item.comment;

import lombok.Generated;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;

@Generated
public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return Comment
                .builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(ItemMapper.toItem(commentDto.getItem()))
                .author(User
                        .builder()
                        .id(commentDto.getAuthorId())
                        .build())
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(ItemMapper.toItemDto(comment.getItem()))
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
