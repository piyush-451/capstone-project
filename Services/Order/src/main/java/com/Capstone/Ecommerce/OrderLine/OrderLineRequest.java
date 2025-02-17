package com.Capstone.Ecommerce.OrderLine;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record OrderLineRequest(
        @NotEmpty(message = "product id cannot be empty")
        Long id,

        @Positive(message = "product quantity must be positive")
        double quantity
) {
}
