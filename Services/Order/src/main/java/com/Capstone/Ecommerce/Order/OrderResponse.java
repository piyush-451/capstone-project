package com.Capstone.Ecommerce.Order;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        Long userID,
        BigDecimal amount,
        PaymentMethod PaymentMethod
) {
}
