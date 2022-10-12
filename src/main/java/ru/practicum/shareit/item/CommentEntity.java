package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "comments")
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text_comment")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private LocalDateTime created;

}
