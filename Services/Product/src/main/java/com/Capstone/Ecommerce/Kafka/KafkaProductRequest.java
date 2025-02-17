package com.Capstone.Ecommerce.Kafka;

public record KafkaProductRequest(
        Long id,
        double availableQuantity
) {
}
