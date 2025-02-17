package com.Capstone.Ecommerce.Product;

import java.math.BigInteger;
import java.util.Set;

public record ProductResponse(
        Long id,

        String name,

        double availableQuantity,

        BigInteger price
) {
}
