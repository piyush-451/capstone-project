package com.Capstone.Ecommerce.Order;

import com.Capstone.Ecommerce.OrderLine.OrderLineRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,

        @NotEmpty(message = "you must by at least one product")
        List<OrderLineRequest> orderLine
) {
}
