package com.Capstone.Ecommerce.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotNull(message = "username cannot be null")
        @NotEmpty(message = "username cannot be empty")
        @Size(min = 5, message = "username should have at least 5 characters")
        String username,

        @NotNull(message = "password cannot be null")
        @NotEmpty(message = "password cannot be empty")
        @Size(min = 5, message = "password should have at least 5 characters")
        String password,

        @NotNull(message = "email cannot be null")
        @Email(message = "Email should be valid")
        String email
) {}

