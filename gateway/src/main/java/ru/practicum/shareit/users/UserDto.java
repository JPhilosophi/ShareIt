package ru.practicum.shareit.users;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Incorrect email format")
    @NotNull
    @NotEmpty
    @NotBlank
    private String email;
}
