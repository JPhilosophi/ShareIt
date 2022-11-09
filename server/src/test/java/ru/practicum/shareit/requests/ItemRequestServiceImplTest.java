package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequestEntity;
import ru.practicum.shareit.requests.model.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.requests.service.ItemRequestImpl;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestImpl itemRequest;
    private ItemEntity item;
    private UserEntity user;
    private BookingEntity bookingEntity;
    private ItemRequestEntity itemRequestEntity;
    private ItemRequestInputDto inputDto;
    private ItemRequestOutputDto itemOutput;
    private ItemDto itemDto;
    private Integer from = 0;
    private Integer size = 10;

    @BeforeEach
    void setUp() {
        LocalDateTime created = LocalDateTime.now().withNano(0);

        item = new ItemEntity();
        item.setId(2L);
        item.setName("Санки");
        item.setDescription("Санки");
        item.setOwnerId(1L);
        item.setRequestId(1L);
        item.setAvailable(true);
        item.setNextBookingId(1L);
        item.setLastBookingId(2L);

        user = new UserEntity();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@email.com");

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setBookerId(2L);
        bookingEntity.setItemId(2L);
        bookingEntity.setStatus(Status.APPROVED);
        bookingEntity.setStart(LocalDateTime.now().plusDays(1));
        bookingEntity.setEnd(LocalDateTime.now().plusDays(2));

        itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(1L);
        itemRequestEntity.setRequesterId(user.getId());
        itemRequestEntity.setDescription("какой-то предмет");
        itemRequestEntity.setCreated(created);

        inputDto = new ItemRequestInputDto();
        inputDto.setDescription("какой-то предмет");

        itemOutput = new ItemRequestOutputDto();
        itemOutput.setId(1L);
        itemOutput.setItems(null);
        itemOutput.setDescription("какой-то предмет");
        itemOutput.setCreated(created);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("катер");
        itemDto.setDescription("катер для рыбалки");
        itemDto.setAvailable(true);
        itemDto.setRequestId(3L);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);
    }

    @Test
    void createRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequestEntity.class))).thenReturn(itemRequestEntity);
        var result = itemRequest.createRequest(inputDto, user.getId());
        verify(itemRequestRepository, times(1)).save(any(ItemRequestEntity.class));
        assertNotNull(result);
        assertEquals(result.getId(), itemRequestEntity.getId());
    }

    @Test
    void findItemRequestById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemRequestEntity.getId())).thenReturn(Optional.of(itemRequestEntity));
        when(itemRepository.findAllByRequestId(itemRequestEntity.getId())).thenReturn(List.of(item));
        var result = itemRequest.findItemRequestById(user.getId(), itemRequestEntity.getId());
        verify(itemRequestRepository, times(1)).findById(itemRequestEntity.getId());
        assertNotNull(result);
        assertEquals(result.getId(), itemRequestEntity.getId());
        assertEquals(result.getDescription(), itemRequestEntity.getDescription());
    }

    @Test
    void findAllItemRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllById(List.of(user.getId()))).thenReturn(List.of(itemRequestEntity));
        when(itemRepository.findAllByRequestId(user.getId())).thenReturn(List.of(item));
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        var result = itemRequest.findAllItemRequest(user.getId());
        verify(itemRequestRepository, times(1)).findAllById(List.of(user.getId()));
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void findPage() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        when(itemRequestRepository.findAllByRequesterIdNot(user.getId(), pageable)).thenReturn(List.of(itemRequestEntity));
        when(itemRepository.findAllByRequestId(itemRequestEntity.getId())).thenReturn(List.of(item));
        var result = itemRequest.findPage(user.getId(), from, size);
        verify(itemRequestRepository, times(1)).findAllByRequesterIdNot(itemRequestEntity.getId(), pageable);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void badRequest() {
        inputDto.setDescription("");
        assertThrows(BadRequestException.class, () -> itemRequest.createRequest(inputDto, user.getId()));
    }

    @Test
    void cantFindUser() {
        user.setId(100L);
        assertThrows(NotFoundException.class, () -> itemRequest.createRequest(inputDto, user.getId()));
    }
}