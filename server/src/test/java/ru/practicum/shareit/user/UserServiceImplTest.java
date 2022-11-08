package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private  UserEntity userEntity;
    private UserDto user;
    private UserDto userDto;
    private UserDto userWithoutName;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setName("Vlad");
        user.setEmail("blad@vlad.ru");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Vlad");
        userEntity.setEmail("blad@vlad.ru");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Vlad");

        userWithoutName = new UserDto();
        userWithoutName.setId(1L);
        userWithoutName.setEmail("blad@vlad.ru");

    }

    @Test
    void findUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userEntity));

        var result = userService.get(anyLong());

        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void findUserByIdThrowException() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.get(anyLong()));
    }


    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        var result = userService.getUsers();
        verify(userRepository, times(1)).findAll();

        assertEquals(1, result.size());
    }

    @Test
    void delete() {
        userService.delete(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void create() {
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        var result = UserMapper.mapToUserEntity(userService.create(user));
        verify(userRepository, times(1)).save(userEntity);
        assertNotNull(result);
        assertEquals(userEntity, result);
    }

    @Test
    void updateWithoutEmail() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
        var result = userService.update(userEntity.getId(), userDto);
        assertEquals(userDto.getEmail(), userEntity.getEmail());
    }

    @Test
    void updateWithoutName() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
        var result = userService.update(userEntity.getId(), userWithoutName);
        assertEquals(userDto.getName(), userEntity.getName());
    }

    @Test
    void create_throws_whenSaveThrow() {
        when(userRepository.save(userEntity)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    void update() {
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        var result = UserMapper.mapToUserEntity(userService.update(user.getId(), user));
        verify(userRepository, times(1)).save(userEntity);
        assertNotNull(result);
        assertEquals(userEntity, result);
    }

    @Test
    void update_throws_whenUpdateThrow() {
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(RuntimeException.class, () -> userService.update(1L, user));
    }

}