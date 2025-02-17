package com.Capstone.Ecommerce.Product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;
import java.util.Set;

public record ProductRequest(
        @NotEmpty(message = "product name cannot be empty")
        String name,

        @NotEmpty(message = "product name cannot be empty")
        @Size(min = 20 ,message = "description must be at least 20 char")
        String description,

        @Positive
        @NotNull(message = "quantity can not be null")
        double availableQuantity,

        @Positive
        @NotNull(message = "price can not be null")
        BigInteger price,

        Set<String> categories
) {
}
