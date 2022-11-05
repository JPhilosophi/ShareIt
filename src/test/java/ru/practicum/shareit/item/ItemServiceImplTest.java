package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentEntity;
import ru.practicum.shareit.item.model.CommentInto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ItemServiceImpl itemService;
    private ItemEntity item;
    private ItemDto itemWithoutName;
    private ItemDto itemWithoutAvailable;
    private ItemDto itemOnlyName;
    private CommentEntity comment;
    private CommentInto commentInto;
    private UserEntity author;
    private UserEntity user;
    private BookingEntity bookingEntity;
    private BookingEntity last;
    private BookingEntity next;

    @BeforeEach
    void setUp() {
        LocalDateTime created = LocalDateTime.now().withNano(0);

        last = new BookingEntity();
        last.setId(1L);
        last.setBookerId(3L);
        last.setItemId(2L);
        last.setStatus(Status.APPROVED);
        last.setStart(LocalDateTime.now().plusDays(1));
        last.setEnd(LocalDateTime.now().plusDays(2));

        next = new BookingEntity();
        next.setId(2L);
        next.setBookerId(3L);
        next.setItemId(2L);
        next.setStatus(Status.APPROVED);
        next.setStart(LocalDateTime.now().plusDays(1));
        next.setEnd(LocalDateTime.now().plusDays(2));

        item = new ItemEntity();
        item.setId(2L);
        item.setName("Санки");
        item.setDescription("Санки");
        item.setOwnerId(1L);
        item.setRequestId(1L);
        item.setAvailable(true);
        item.setNextBookingId(next.getId());
        item.setLastBookingId(last.getId());

        itemWithoutName = new ItemDto();
        itemWithoutName.setId(2L);
        itemWithoutName.setAvailable(true);

        itemWithoutAvailable = new ItemDto();
        itemWithoutAvailable.setId(2L);
        itemWithoutAvailable.setDescription("Санки");

        itemOnlyName = new ItemDto();
        itemOnlyName.setId(2L);
        itemOnlyName.setName("Санки");

        author = new UserEntity();
        author.setId(2L);
        author.setName("Name");
        author.setEmail("emai@email.com");

        user = new UserEntity();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@email.com");

        comment = new CommentEntity();
        comment.setId(1L);
        comment.setText("text");
        comment.setAuthor(author);
        comment.setCreated(created);
        comment.setItem(item);

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setBookerId(2L);
        bookingEntity.setItemId(2L);
        bookingEntity.setStatus(Status.APPROVED);
        bookingEntity.setStart(LocalDateTime.now().plusDays(1));
        bookingEntity.setEnd(LocalDateTime.now().plusDays(2));

        commentInto = new CommentInto();
        commentInto.setId(1L);
        commentInto.setItemId(2L);
        commentInto.setText("text");
        commentInto.setUserId(author.getId());
    }

    @Test
    void create() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);
        var result = itemService.create(user.getId(), ItemMapper.mapToItemDto(item));
        verify(itemRepository, times(1)).save(item);
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
    }

    @Test
    void update() {
        when(itemRepository.findOne(Example.of(item))).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(item)).thenReturn(item);
        var result = itemService.update(user.getId(), item.getId(), ItemMapper.mapToItemDto(item));
        verify(itemRepository, times(1)).saveAndFlush(item);
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
    }

    @Test
    void updateWithoutNameAndDescription() {
        when(itemRepository.findOne(Example.of(item))).thenReturn(Optional.of(item));
        var resutl = itemService.update(user.getId(), itemWithoutName.getId(), itemWithoutName);
        assertEquals(resutl.getName(), item.getName());
        assertEquals(resutl.getAvailable(), item.getAvailable());
    }

    @Test
    void updateWithoutNameAndAvailable() {
        when(itemRepository.findOne(Example.of(item))).thenReturn(Optional.of(item));
        var resutl = itemService.update(user.getId(), itemWithoutName.getId(), itemWithoutAvailable);
        assertEquals(resutl.getName(), item.getName());
        assertEquals(resutl.getDescription(), item.getDescription());
    }

    @Test
    void updateOnlyName() {
        when(itemRepository.findOne(Example.of(item))).thenReturn(Optional.of(item));
        var resutl = itemService.update(user.getId(), itemWithoutName.getId(), itemOnlyName);
        assertEquals(resutl.getName(), item.getName());
        assertEquals(resutl.getDescription(), item.getDescription());
    }

    @Test
    void getById() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemId(item.getId())).thenReturn(List.of(bookingEntity));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));
        var result = itemService.getById(user.getId(), item.getId());
        verify(itemRepository, times(1)).findById(item.getId());
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
    }

    @Test
    void get() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdIn(List.of(item.getId()))).thenReturn(List.of(bookingEntity));
        lenient().when(bookingRepository.findById(last.getId())).thenReturn(Optional.of(last));
        lenient().when(bookingRepository.findById(next.getId())).thenReturn(Optional.of(next));
        var result = itemService.get(user.getId());
        verify(itemRepository, times(1)).findAllByOwnerId(user.getId());
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void search() {
        when(itemRepository.search("text")).thenReturn(List.of(item));
        var result = itemService.search(user.getId(), "text");
        verify(itemRepository, times(1)).search("text");
        assertNotNull(result);
    }

    @Test
    void createComment() {
        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(item.getId())).thenReturn(Optional.of(bookingEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comment);
        var result = itemService.createComment(commentInto, author.getId(),item.getId());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
        assertNotNull(result);
        assertEquals(result.getId(), comment.getId());
    }

    @Test
    void getNotFoundExceptionItems() {
        assertThrows(NotFoundException.class, () -> itemService.get(1L));
    }

}