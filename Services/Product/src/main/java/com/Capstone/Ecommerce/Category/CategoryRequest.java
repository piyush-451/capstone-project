package com.Capstone.Ecommerce.Category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotEmpty(message = "category name can not be empty")
        @Size(min = 3,message = "category length must be greater than 3")
        String name,

        String description
) {
}
