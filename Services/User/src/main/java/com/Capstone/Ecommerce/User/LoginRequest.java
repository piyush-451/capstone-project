package com.Capstone.Ecommerce.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotNull(message = "password cannot be null")
        @NotEmpty(message = "password cannot be empty")
        @Size(min = 5, message = "password should have at least 5 characters")
        String password,

        @NotNull(message = "email cannot be null")
        @Email(message = "Email should be valid")
        String email
) {}

