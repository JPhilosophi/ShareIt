package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.ShortItemDto;
import ru.practicum.shareit.user.model.ShortUserDto;
import ru.practicum.shareit.user.model.UserEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserEntity user;
    private BookingOutputDto bookingOutputDto;
    private BookingInputDto bookingInputDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        start = LocalDateTime.now().plusMinutes(1);
        end = LocalDateTime.now().plusDays(1);

        user = new UserEntity();
        user.setId(1L);
        user.setName("name");
        user.setEmail("name@email.com");

        bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(1L);
        bookingOutputDto.setStart(start);
        bookingOutputDto.setEnd(end);
        bookingOutputDto.setStatus(Status.WAITING);
        bookingOutputDto.setBooker(new ShortUserDto(1L));
        bookingOutputDto.setItem(new ShortItemDto(1L, "vilka"));

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setItemId(1L);
        bookingInputDto.setStart(start);
        bookingInputDto.setEnd(end);
    }

    @Test
    void create() throws Exception {
        when(bookingService.create(1L, bookingInputDto)).thenReturn(bookingOutputDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.status", is(bookingOutputDto.getStatus().toString())));
    }

    @Test
    void update() throws Exception {
        when(bookingService.update(user.getId(), 1L, true)).thenReturn(bookingOutputDto);
        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", user.getId())
                        .param("approved", String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getById(1L, 1L)).thenReturn(bookingOutputDto);
        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAllByBooker() throws Exception {
        when(bookingService.getAllByBooker(user.getId(), null, 0, 10)).thenReturn(List.of(bookingOutputDto));
        mvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByOwner() throws Exception {
        when(bookingService.getAllByOwner(user.getId(), null, 0, 10)).thenReturn(List.of(bookingOutputDto));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}