package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {
    @InjectMocks
    private CommentEntity commentEntity;
    private CommentDto commentDto;
    private ItemEntity itemEntity;
    private UserEntity author;

    @BeforeEach
    void setUp() {
        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("name");
        itemEntity.setDescription("desc");
        itemEntity.setOwnerId(1L);
        itemEntity.setAvailable(true);
        itemEntity.setRequestId(1L);

        author = new UserEntity();
        author.setId(1L);
        author.setName("Vlad");
        author.setEmail("blad@vlad.ru");

        commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setText("text");
        commentEntity.setCreated(LocalDateTime.now());
        commentEntity.setItem(itemEntity);
        commentEntity.setAuthor(author);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setAuthorName(author.getName());
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setText("text");
    }

    @Test
    void toComment() {
        CommentEntity commentEntity = CommentMapper.toComment(commentDto);
        assertEquals(commentEntity.getId(), commentDto.getId());
        assertNotNull(commentEntity);
    }

    @Test
    void toCommentDto() {
        CommentDto commentDto = CommentMapper.toCommentDto(commentEntity);
        assertEquals(commentDto.getId(), commentEntity.getId());
        assertNotNull(commentDto);
    }

    @Test
    void toListCommentDto() {
        List<CommentDto> result = CommentMapper.toListCommentDto(List.of(commentEntity));
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }
}
