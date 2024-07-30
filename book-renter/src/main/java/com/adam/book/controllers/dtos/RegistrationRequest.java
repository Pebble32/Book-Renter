package com.adam.book.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotBlank(message = "Firstname can't be empty")
    @NotEmpty(message = "Firstname can't be empty")
    private String firstName;

    @NotBlank(message = "Lastname can't be empty")
    @NotEmpty(message = "Lastname can't be empty")
    private String lastName;

    @Email(message = "Email is not correctly formated")
    @NotBlank(message = "Email can't be empty")
    @NotEmpty(message = "Email can't be empty")
    private String email;

    @Size(min = 8, message = "Password should be at least 8 characters long")
    @NotBlank(message = "Password can't be empty")
    @NotEmpty(message = "Password can't be empty")
    private String password;
}
