package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("UserRepository")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto user) {
        UserEntity userEntity = userRepository.save(UserMapper.mapToUserEntity(user));
        return UserMapper.mapToUserDto(userRepository.findById(userEntity.getId()).orElseThrow());
    }

    @Override
    public UserDto update(Long id, UserDto user) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getEmail() == null) {
            user.setId(userEntity.getId());
            user.setEmail(userEntity.getEmail());
            user.setName(user.getName());
        } else if (user.getName() == null) {
            user.setId(userEntity.getId());
            user.setName(userEntity.getName());
            user.setEmail(user.getEmail());
        } else {
            user.setId(userEntity.getId());
            user.setName(user.getName());
            user.setEmail(user.getEmail());
        }
        userRepository.save(UserMapper.mapToUserEntity(user));
        return user;
    }

    @Override
    public UserDto get(Long id) {
        return UserMapper.mapToUserDto(userRepository.
                findById(id).
                orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity user : userRepository.findAll()) {
            userDtos.add(UserMapper.mapToUserDto(user));
        }
        return userDtos;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
