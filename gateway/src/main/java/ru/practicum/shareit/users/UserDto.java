package ru.practicum.shareit.users;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;
    @NotNull(message = "Name can't be null")
    private String name;
    @Email(message = "Incorrect email format")
    @NotNull(message = "Please enter email")
    @NotEmpty(message = "Please enter email")
    private String email;
}
