package ru.practicum.shareit.requests.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ru.practicum.ru.practicum.shareit.requests")
public class ItemRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDateTime created;
    @Column(name = "requestor_id")
    private Long requesterId;
}
