package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    private UserEntity booker;
    private UserEntity owner;
    private BookingEntity bookingEntity;
    private ItemEntity itemEntity;
    private BookingInputDto bookingInputDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        itemEntity = new ItemEntity();
        itemEntity.setId(2L);
        itemEntity.setName("Сноуборд");
        itemEntity.setDescription("Новый сноуборд");
        itemEntity.setOwnerId(1L);
        itemEntity.setRequestId(1L);
        itemEntity.setAvailable(true);
        itemEntity.setNextBookingId(1L);
        itemEntity.setLastBookingId(2L);

        booker = new UserEntity();
        booker.setId(2L);
        booker.setName("Name");
        booker.setEmail("emai@email.com");

        owner = new UserEntity();
        owner.setId(1L);
        owner.setName("Name");
        owner.setEmail("emai@email.com");

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setBookerId(2L);
        bookingEntity.setItemId(2L);
        bookingEntity.setStatus(Status.WAITING);
        bookingEntity.setStart(LocalDateTime.now().plusDays(1));
        bookingEntity.setEnd(LocalDateTime.now().plusDays(2));

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setItemId(2L);
        bookingInputDto.setStart(LocalDateTime.now().plusDays(1));
        bookingInputDto.setEnd(LocalDateTime.now().plusDays(2));
        int from = 0;
        int size = 10;
        pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start"));
    }


    @Test
    void create() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemEntity.getId())).thenReturn(Optional.of(itemEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
        var result = bookingService.create(booker.getId(), bookingInputDto);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
        assertEquals(result.getItem().getId(), bookingEntity.getItemId());
        assertSame(result.getStatus(), Status.WAITING);
    }

    @Test
    void approvedNull() {
        assertThrows(BadRequestException.class, () -> bookingService.update(1L, 2L, null));
    }

    @Test
    void create_throws_whenWrongBooker() {
        assertThrows(NotFoundException.class, () -> bookingService.create(19000L, bookingInputDto));
    }

    @Test
    void create_throws_whenItemIsUnavailable() {
        bookingEntity.setStatus(Status.REJECTED);
        bookingEntity.setBookerId(2L);
        booker.setId(3L);
        assertNotEquals(bookingEntity.getBookerId(), booker.getId());
    }

    @Test
    void throwsCantFindItem() {
        when(itemRepository.findById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemRepository.findById(anyLong()));
    }


    @Test
    void updateBooking() {
        when(itemRepository.findById(itemEntity.getId())).thenReturn(Optional.of(itemEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
        when(bookingRepository.findOne(Example.of(bookingEntity))).thenReturn(Optional.of(bookingEntity));
        var result = bookingService.update(owner.getId(), bookingEntity.getId(), true);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
        assertEquals(result.getItem().getId(), bookingEntity.getItemId());
        assertSame(result.getStatus(), Status.APPROVED);
    }

    @Test
    void getAllByBooker() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByUserId(booker.getId())).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.findAllByBookerId(booker.getId(), pageable)).thenReturn(List.of(bookingEntity));
        when(itemRepository.findById(bookingEntity.getItemId())).thenReturn(Optional.of(itemEntity));
        var result = bookingService.getAllByBooker(booker.getId(), State.ALL, 0, 10);
        verify(bookingRepository, times(1)).findAllByBookerId(booker.getId(), pageable);
        assertNotNull(result);
    }

    @Test
    void getAllByOwner() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwnerId(owner.getId())).thenReturn(List.of(itemEntity));
        when(bookingRepository.findAllByItemId(itemEntity.getId(), pageable)).thenReturn(List.of(bookingEntity));
        when(itemRepository.findById(bookingEntity.getItemId())).thenReturn(Optional.of(itemEntity));
        var result = bookingService.getAllByOwner(owner.getId(), State.ALL, 0, 10);
        verify(bookingRepository, times(1)).findAllByItemId(itemEntity.getId(), pageable);
        assertNotNull(result);
    }

    @Test
    void getById() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(bookingEntity.getId())).thenReturn(Optional.of(bookingEntity));
        when(itemRepository.findById(bookingEntity.getItemId())).thenReturn(Optional.of(itemEntity));
        var result = bookingService.getById(booker.getId(), bookingEntity.getId());
        verify(bookingRepository, times(2)).findById(anyLong());
        assertEquals(result.getItem().getId(), bookingEntity.getItemId());
    }

}

