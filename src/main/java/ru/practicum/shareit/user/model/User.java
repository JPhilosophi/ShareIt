package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private static Long count = 1L;
    @NotNull(message = "Id can't be null")
    private Long id = 0L;
    @NotNull(message = "Name can't be null")
    private String name;
    @Email(message = "Incorrect email format")
    @NotNull(message = "Please enter email")
    @NotEmpty(message = "Please enter email")
    private String email;


    public void getNextId() {
       id = count++;
    }
}
