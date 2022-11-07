package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.requests.controller.ItemRequestController;
import ru.practicum.shareit.requests.model.ItemRequestEntity;
import ru.practicum.shareit.requests.model.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.UserEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService requestService;
    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private LocalDateTime time;
    private ItemRequestOutputDto requestOutputDto;
    private ItemRequestInputDto requestInputDto;
    private ItemRequestEntity itemRequest;
    private UserEntity user;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("предмет");

        requestOutputDto = new ItemRequestOutputDto();
        requestOutputDto.setId(1L);
        requestOutputDto.setDescription("какой-то текс");
        requestOutputDto.setItems(List.of(itemDto));

        requestInputDto = new ItemRequestInputDto();
        requestInputDto.setDescription("добавь уже!");

        user = new UserEntity();
        user.setId(1L);
        user.setName("Vovan");
        user.setEmail("vovka@car.br");
    }

    @Test
    void create() throws Exception {
        when(requestService.createRequest(any(ItemRequestInputDto.class), anyLong())).thenReturn(requestOutputDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestOutputDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestOutputDto.getDescription())))
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void findAll() throws Exception {
        List<ItemRequestOutputDto> requestOutputDtos = List.of(requestOutputDto);
        when(requestService.findAllItemRequest(anyLong()))
                .thenReturn(requestOutputDtos);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        is(requestOutputDtos.stream().findFirst().get().getId()), Long.class))
                .andExpect(jsonPath("$[0].description",
                        is(requestOutputDtos.stream().findFirst().get().getDescription())))
                .andExpect(jsonPath("$[0].items").exists());
    }

    @Test
    void findPageOfThisSize() throws Exception {
        List<ItemRequestOutputDto> requestOutputDtos = List.of(requestOutputDto);
        when(requestService.findPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(requestOutputDtos);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",
                        is(requestOutputDtos.stream().findFirst().get().getId()), Long.class))
                .andExpect(jsonPath("$[0].description",
                        is(requestOutputDtos.stream().findFirst().get().getDescription())))
                .andExpect(jsonPath("$[0].items").exists());
    }
}